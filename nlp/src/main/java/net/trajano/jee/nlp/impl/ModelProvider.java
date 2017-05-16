package net.trajano.jee.nlp.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration.GraphBuilder;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.graph.rnn.DuplicateToTimeSeriesVertex;
import org.deeplearning4j.nn.conf.graph.rnn.LastTimeStepVertex;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.EmbeddingLayer;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import net.trajano.jee.domain.dao.LobDAO;

/**
 * This is a {@link ComputationGraph} provider. It will periodically save it's
 * data to the database type.
 *
 * @author Archimedes Trajano
 */
@Singleton
@Startup
@ApplicationScoped
public class ModelProvider {

    private static final int EMBEDDING_WIDTH = 128; // one-hot vectors will be embedded to more dense vectors with this width

    private static final String GRAPH_ID = "dl4jComputationGraph";

    private static final int HIDDEN_LAYER_WIDTH = 512; // this is purely empirical, affects performance and VRAM requirement

    private static final String INPUT_DECODER = "decoderInput";

    private static final String INPUT_LINE = "inputLine";

    private static final double LEARNING_RATE = 1e-1;

    private static final Logger LOG = Logger.getLogger("net.trajano.jee.nlp");

    private static final double RMS_DECAY = 0.95;

    private static final int TBPTT_SIZE = 25;

    private final Map<String, Double> dict = new HashMap<>();

    private LobDAO lobDAO;

    private ComputationGraph net;

    @Lock(LockType.WRITE)
    @PreDestroy
    public void close() {

        persistCurrentNetwork();
    }

    /**
     * This will load the current graph from the database or construct a new one
     * if it is not found.
     *
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    @PostConstruct
    public void loadFromDatabase() {

        try (final InputStream is = new GZIPInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("dict.properties.gz"))) {
            final Properties p = new Properties();
            p.load(is);
            for (final Entry<Object, Object> e : p.entrySet()) {
                dict.put((String) e.getKey(), Double.valueOf((String) e.getValue()));
            }
        } catch (final IOException e1) {
            throw new PersistenceException(e1);
        }

        final InputStream dbData = lobDAO.getInputStream(GRAPH_ID);
        if (dbData == null) {

            final NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder()
                .iterations(1).learningRate(LEARNING_RATE)
                .rmsDecay(RMS_DECAY)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .miniBatch(true)
                .updater(Updater.RMSPROP)
                .weightInit(WeightInit.XAVIER)
                .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer);

            final GraphBuilder graphBuilder = builder.graphBuilder()
                .pretrain(false)
                .backprop(true).backpropType(BackpropType.Standard)
                .tBPTTBackwardLength(TBPTT_SIZE)
                .tBPTTForwardLength(TBPTT_SIZE)
                .addInputs(INPUT_LINE, INPUT_DECODER)
                .setInputTypes(InputType.recurrent(dict.size()), InputType.recurrent(dict.size()))
                .addLayer("embeddingEncoder", new EmbeddingLayer.Builder().nIn(dict.size()).nOut(EMBEDDING_WIDTH).build(), INPUT_LINE)
                .addLayer("encoder",
                    new GravesLSTM.Builder().nIn(EMBEDDING_WIDTH).nOut(HIDDEN_LAYER_WIDTH).activation(Activation.TANH).build(),
                    "embeddingEncoder")
                .addVertex("thoughtVector", new LastTimeStepVertex(INPUT_LINE), "encoder")
                .addVertex("dup", new DuplicateToTimeSeriesVertex(INPUT_DECODER), "thoughtVector")
                .addVertex("merge", new MergeVertex(), INPUT_DECODER, "dup")
                .addLayer("decoder",
                    new GravesLSTM.Builder().nIn(dict.size() + HIDDEN_LAYER_WIDTH).nOut(HIDDEN_LAYER_WIDTH).activation(Activation.TANH)
                        .build(),
                    "merge")
                .addLayer("output", new RnnOutputLayer.Builder().nIn(HIDDEN_LAYER_WIDTH).nOut(dict.size()).activation(Activation.SOFTMAX)
                    .lossFunction(LossFunctions.LossFunction.MCXENT).build(), "decoder")
                .setOutputs("output");

            net = new ComputationGraph(graphBuilder.build());
            net.init();

            persistCurrentNetwork();
        } else {
            try {
                net = ModelSerializer.restoreComputationGraph(dbData, true);
            } catch (final IOException e) {
                LOG.log(Level.SEVERE, "Unable to restore network, rebuilding", e);
                lobDAO.remove(GRAPH_ID);
                loadFromDatabase();
            }
        }

    }

    @Schedule(minute = "*/10",
        hour = "*")
    @Lock(LockType.READ)
    public void persistCurrentNetwork() {

        try {
            final File tempFile = File.createTempFile("model", ".zip");
            try (OutputStream os = new FileOutputStream(tempFile)) {
                ModelSerializer.writeModel(net, os, true);
            }
            try (InputStream is = new FileInputStream(tempFile)) {
                lobDAO.update(GRAPH_ID, is);
            }
            if (!tempFile.delete()) {
                LOG.warning(String.format("Unable to delete temporary file %s", tempFile));
            }
        } catch (final IOException e) {
            LOG.throwing(this.getClass().getName(), "persistCurrentGraph", e);
            throw new PersistenceException(e);
        }

    }

    @Inject
    public void setLobDAO(final LobDAO lobDAO) {

        this.lobDAO = lobDAO;
    }
}
