package net.trajano.jee.nlp.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import net.trajano.jee.nlp.AnalysisResult;
import net.trajano.jee.nlp.SentenceResult;

public class AnalysisResultTest {

    @Test
    public void testApiObjects() {

        final AnalysisResult analysisResult = new AnalysisResult();
        final SentenceResult r = new SentenceResult();
        r.setParseTree("abc");
        r.setLemmas(Arrays.asList("lema", "lema", "duck"));
        analysisResult.addSentenceResult(r);
        assertEquals("abc", r.getParseTree());
        assertEquals(Arrays.asList("lema", "lema", "duck"), r.getLemmas());
        assertEquals(1, analysisResult.getSentences().size());

    }
}
