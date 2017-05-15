package net.trajano.jee.nlp.test;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.junit.Test;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import net.trajano.jee.nlp.impl.TensorGraphProvider;

public class TensorGraphProviderTest extends BaseIntegrationTest {

    @Test
    public void testProvider() throws Exception {

        {
            final Set<Bean<?>> beans = beanManager.getBeans(TensorGraphProvider.class);
            @SuppressWarnings("unchecked")
            final Bean<TensorGraphProvider> bean = (Bean<TensorGraphProvider>) beans.iterator().next();
            final CreationalContext<TensorGraphProvider> cc = beanManager.createCreationalContext(null);
            final TensorGraphProvider provider = (TensorGraphProvider) beanManager.getReference(bean, TensorGraphProvider.class, cc);
            try (final Tensor t = Tensor.create("Hello world".getBytes("UTF-8"))) {
                provider.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();
            }
            provider.persistCurrentGraph();
        }
        {
            final Set<Bean<?>> beans = beanManager.getBeans(Session.class);
            @SuppressWarnings("unchecked")
            final Bean<Session> bean = (Bean<Session>) beans.iterator().next();
            final CreationalContext<Session> cc = beanManager.createCreationalContext(bean);
            try (final Session session = (Session) beanManager.getReference(bean, Session.class, cc);
                final Tensor output = session.runner().fetch("MyConst").run().get(0)) {
                assertEquals("Hello world", new String(output.bytesValue(), "UTF-8"));
            }
        }

    }
}
