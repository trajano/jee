package net.trajano.jee.nlp.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import net.trajano.jee.nlp.impl.TensorGraphProvider;

public class TensorGraphProviderTest {

    @Test
    public void testProvider() throws Exception {

        final TensorGraphProvider provider = new TensorGraphProvider();
        provider.setLobDAO(new FakeLobDAO());
        provider.loadFromDatabase();

        {
            try (final Tensor t = Tensor.create("Hello world".getBytes("UTF-8"))) {
                provider.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();
            }
            provider.persistCurrentGraph();
        }
        {
            try (final Session session = provider.newSession();
                final Tensor output = session.runner().fetch("MyConst").run().get(0)) {
                assertEquals("Hello world", new String(output.bytesValue(), "UTF-8"));
            }
        }
        provider.closeGraph();

    }
}
