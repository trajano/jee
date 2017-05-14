package net.trajano.jee.nlp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnalysisResult implements
    Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -6437829684620598155L;

    private final List<SentenceResult> sentences = new ArrayList<>();

    public void addSentenceResult(final SentenceResult r) {

        sentences.add(r);
    }

    public List<SentenceResult> getSentences() {

        return Collections.unmodifiableList(sentences);
    }
}
