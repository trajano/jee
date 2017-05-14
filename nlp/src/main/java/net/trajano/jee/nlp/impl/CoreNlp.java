package net.trajano.jee.nlp.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import net.trajano.jee.nlp.AnalysisResult;
import net.trajano.jee.nlp.Nlp;
import net.trajano.jee.nlp.SentenceResult;

@Local
@Stateless
public class CoreNlp implements
    Nlp {

    @Override
    public AnalysisResult analyze(final String text) {

        final Document doc = new Document(text);
        final AnalysisResult ret = new AnalysisResult();
        for (final Sentence sent : doc.sentences()) {

            final SentenceResult sentenceResult = new SentenceResult();
            sentenceResult.setLemmas(sent.lemmas());
            sentenceResult.setParseTree(sent.parse().toString());
            ret.addSentenceResult(sentenceResult);
        }
        return ret;
    }
}
