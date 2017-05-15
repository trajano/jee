package net.trajano.jee.nlp.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;

public class TensorFlowSampleTest {

    @Test
    public void testTensorFlowSample() throws Exception {

        try (final Graph g = new Graph()) {
            final String value = "Hello from " + TensorFlow.version();

            // Construct the computation graph with a single operation, a constant
            // named "MyConst" with a value "value".
            try (Tensor t = Tensor.create(value.getBytes("UTF-8"))) {
                // The Java API doesn't yet include convenience functions for adding operations.
                g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();
            }

            // Execute the "MyConst" operation in a Session.
            try (final Session s = new Session(g);
                Tensor output = s.runner().fetch("MyConst").run().get(0)) {
                assertEquals(value, new String(output.bytesValue(), "UTF-8"));
            }
        }
    }
}
