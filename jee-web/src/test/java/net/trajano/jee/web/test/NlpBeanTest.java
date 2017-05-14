package net.trajano.jee.web.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.faces.config.InitFacesContext;

import net.trajano.jee.bean.NlpBean;
import net.trajano.jee.nlp.AnalysisResult;
import net.trajano.jee.nlp.Nlp;

public class NlpBeanTest {

    private class FakeContext extends InitFacesContext {

        public FakeContext(final ServletContext sc) {
            super(sc);
            setCurrentInstance(context);
        }

    }

    @Mock
    private FacesContext context;

    @Before
    public void before() {

        MockitoAnnotations.initMocks(this);
        final ServletContext sc = mock(ServletContext.class);
        new FakeContext(sc);
        assertEquals(context, FacesContext.getCurrentInstance());
    }

    @Test
    public void testSimpleAnalysis() throws Exception {

        final NlpBean bean = new NlpBean();
        final Nlp nlp = mock(Nlp.class);
        when(nlp.analyze(anyString())).thenReturn(new AnalysisResult());
        bean.setNlp(nlp);
        bean.setInput("Hello world");
        assertEquals("Hello world", bean.getInput());
        assertFalse(bean.isParsed());
        bean.parse();
        assertNotNull(bean.getResult());
        assertTrue(bean.isParsed());

    }

}
