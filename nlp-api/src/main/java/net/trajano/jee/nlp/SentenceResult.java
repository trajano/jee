package net.trajano.jee.nlp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SentenceResult implements
    Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -274542413178985831L;

    private List<String> lemmas = new ArrayList<>();

    private String parseTree;

    public List<String> getLemmas() {

        return lemmas;
    }

    public String getParseTree() {

        return parseTree;
    }

    public void setLemmas(final List<String> lemmas) {

        this.lemmas = lemmas;
    }

    public void setParseTree(final String parseTree) {

        this.parseTree = parseTree;
    }

}
