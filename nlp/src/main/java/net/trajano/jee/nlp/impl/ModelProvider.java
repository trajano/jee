package net.trajano.jee.nlp.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Logger;

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
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

import net.trajano.jee.domain.dao.LobDAO;

/**
 * This is a tensor graph provider. It will periodically save it's data to the
 * database type.
 *
 * @author Archimedes Trajano
 */
@Singleton
@Startup
@ApplicationScoped
public class ModelProvider {

    private static final long GRAPH_ID = 1L;

    private static final Logger LOG = Logger.getLogger("net.trajano.jee.nlp");

    private LobDAO lobDAO;

    private MultiLayerNetwork network;

    @Lock(LockType.WRITE)
    @PreDestroy
    public void closeGraph() {

        //        graph.close();
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

        final InputStream dbData = lobDAO.getInputStream(GRAPH_ID);
        if (dbData == null) {
            final int numRows = 28;
            final int numColumns = 28;
            final int outputNum = 10; // number of output classes

            final MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                // use stochastic gradient descent as an optimization algorithm
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .iterations(1)
                .learningRate(0.006) //specify the learning rate
                .updater(Updater.NESTEROVS).momentum(0.9) //specify the rate of change of the learning rate.
                .regularization(true).l2(1e-4)
                .list()
                .layer(0, new DenseLayer.Builder() //create the first, input layer with xavier initialization
                    .nIn(numRows * numColumns)
                    .nOut(1000)
                    .activation(Activation.RELU)
                    .weightInit(WeightInit.XAVIER)
                    .build())
                .layer(1, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD) //create hidden layer
                    .nIn(1000)
                    .nOut(outputNum)
                    .activation(Activation.SOFTMAX)
                    .weightInit(WeightInit.XAVIER)
                    .build())
                .pretrain(false).backprop(true) //use backpropagation to adjust weights
                .build();

            network = new MultiLayerNetwork(conf);
            network.init();
            persistCurrentGraph();
        } else {
            try {
                network = ModelSerializer.restoreMultiLayerNetwork(dbData, true);
            } catch (final IOException e) {
                LOG.severe("Unable to restore network, rebuilding");
                LOG.throwing(this.getClass().getName(), "loadFromDatabase", e);
                lobDAO.remove(GRAPH_ID);
                loadFromDatabase();
            }
        }

    }

    @Schedule(minute = "*/10",
        hour = "*")
    @Lock(LockType.READ)
    public void persistCurrentGraph() {

        try {
            final File tempFile = File.createTempFile("model", ".zip");
            try (OutputStream os = new FileOutputStream(tempFile)) {
                ModelSerializer.writeModel(network, os, true);
            }
            try (InputStream is = new FileInputStream(tempFile)) {
                lobDAO.update(GRAPH_ID, is);
            }
            if (!tempFile.delete()) {
                LOG.warning("Unable to delete temporary file " + tempFile);
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
