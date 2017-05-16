package net.trajano.jee.deeplearning.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.MultiDataSet;
import org.nd4j.linalg.dataset.api.MultiDataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;

@SuppressWarnings("serial")
public class CorpusIterator implements
    MultiDataSetIterator {

    /*
     * Motivation: I want to get asynchronous data iteration while not blocking
     * on net.fit() until the end of epoch. I want to checkpoint the network,
     * show intermediate test results and some stats, it would be harder to
     * achieve with listeners I think so this is how I solved the problem. This
     * way the learn process is asynchronous inside one macrobatch and
     * synchronous across all the macrobatches. Macrobatch is a group of
     * minibatches. The iterator is modified so that it reports the end of data
     * when it exhausts a macrobatch. Then it advances (manually) to the next
     * macrobatch.
     */

    private final int batchesPerMacrobatch;

    private final int batchSize;

    private final List<List<Double>> corpus;

    private int currentBatch = 0;

    private int currentMacroBatch = 0;

    private final int dictSize;

    private final int rowSize;

    private final int totalBatches;

    private final int totalMacroBatches;

    public CorpusIterator(final List<List<Double>> corpus,
        final int batchSize,
        final int batchesPerMacrobatch,
        final int dictSize,
        final int rowSize) {
        this.corpus = corpus;
        this.batchSize = batchSize;
        this.batchesPerMacrobatch = batchesPerMacrobatch;
        this.dictSize = dictSize;
        this.rowSize = rowSize;
        totalBatches = (int) Math.ceil((double) corpus.size() / batchSize);
        totalMacroBatches = (int) Math.ceil((double) totalBatches / batchesPerMacrobatch);
    }

    @Override
    public boolean asyncSupported() {

        return true;
    }

    public int batch() {

        return currentBatch;
    }

    private int getMacroBatchByCurrentBatch() {

        return currentBatch / batchesPerMacrobatch;
    }

    @Override
    public boolean hasNext() {

        return currentBatch < totalBatches && getMacroBatchByCurrentBatch() == currentMacroBatch;
    }

    public boolean hasNextMacrobatch() {

        return getMacroBatchByCurrentBatch() < totalMacroBatches && currentMacroBatch < totalMacroBatches;
    }

    @Override
    public MultiDataSet next() {

        return next(batchSize);
    }

    @Override
    public MultiDataSet next(final int num) {

        int i = currentBatch * batchSize;
        final int currentBatchSize = Math.min(batchSize, corpus.size() - i - 1);
        final INDArray input = Nd4j.zeros(currentBatchSize, 1, rowSize);
        final INDArray prediction = Nd4j.zeros(currentBatchSize, dictSize, rowSize);
        final INDArray decode = Nd4j.zeros(currentBatchSize, dictSize, rowSize);
        final INDArray inputMask = Nd4j.zeros(currentBatchSize, rowSize);
        // this mask is also used for the decoder input, the length is the same
        final INDArray predictionMask = Nd4j.zeros(currentBatchSize, rowSize);
        for (int j = 0; j < currentBatchSize; j++) {
            final List<Double> rowIn = new ArrayList<>(corpus.get(i));
            Collections.reverse(rowIn);
            final List<Double> rowPred = new ArrayList<>(corpus.get(i + 1));
            rowPred.add(1.0); // add <eos> token
            // replace the entire row in "input" using NDArrayIndex, it's faster than putScalar(); input is NOT made of one-hot vectors
            // because of the embedding layer that accepts token indexes directly
            input.put(new INDArrayIndex[] {
                NDArrayIndex.point(j),
                NDArrayIndex.point(0),
                NDArrayIndex.interval(0, rowIn.size())
            },
                Nd4j.create(ArrayUtils.toPrimitive(rowIn.toArray(new Double[0]))));
            inputMask.put(new INDArrayIndex[] {
                NDArrayIndex.point(j),
                NDArrayIndex.interval(0, rowIn.size())
            }, Nd4j.ones(rowIn.size()));
            predictionMask.put(new INDArrayIndex[] {
                NDArrayIndex.point(j),
                NDArrayIndex.interval(0, rowPred.size())
            },
                Nd4j.ones(rowPred.size()));
            // prediction (output) and decode ARE one-hots though, I couldn't add an embedding layer on top of the decoder and I'm not sure
            // it's a good idea either
            final double predOneHot[][] = new double[dictSize][rowPred.size()];
            final double decodeOneHot[][] = new double[dictSize][rowPred.size()];
            decodeOneHot[2][0] = 1; // <go> token
            int predIdx = 0;
            for (final Double pred : rowPred) {
                predOneHot[pred.intValue()][predIdx] = 1;
                if (predIdx < rowPred.size() - 1) { // put the same vals to decode with +1 offset except the last token that is <eos>
                    decodeOneHot[pred.intValue()][predIdx + 1] = 1;
                }
                ++predIdx;
            }
            prediction.put(new INDArrayIndex[] {
                NDArrayIndex.point(j),
                NDArrayIndex.interval(0, dictSize),
                NDArrayIndex.interval(0, rowPred.size())
            }, Nd4j.create(predOneHot));
            decode.put(new INDArrayIndex[] {
                NDArrayIndex.point(j),
                NDArrayIndex.interval(0, dictSize),
                NDArrayIndex.interval(0, rowPred.size())
            }, Nd4j.create(decodeOneHot));
            ++i;
        }
        ++currentBatch;
        return new org.nd4j.linalg.dataset.MultiDataSet(new INDArray[] {
            input,
            decode
        }, new INDArray[] {
            prediction
        },
            new INDArray[] {
                inputMask,
                predictionMask
            }, new INDArray[] {
                predictionMask
            });
    }

    public void nextMacroBatch() {

        ++currentMacroBatch;
    }

    @Override
    public void reset() {

        // but we still can do it manually before the epoch starts
        currentBatch = 0;
        currentMacroBatch = 0;
    }

    @Override
    public boolean resetSupported() {

        // we don't want this iterator to be reset on each macrobatch pseudo-epoch
        return false;
    }

    public void setCurrentBatch(final int currentBatch) {

        this.currentBatch = currentBatch;
        currentMacroBatch = getMacroBatchByCurrentBatch();
    }

    @Override
    public void setPreProcessor(final MultiDataSetPreProcessor preProcessor) {

    }

    public int totalBatches() {

        return totalBatches;
    }

}
