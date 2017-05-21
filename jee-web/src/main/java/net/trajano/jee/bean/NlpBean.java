package net.trajano.jee.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import net.trajano.jee.nlp.AnalysisResult;
import net.trajano.jee.nlp.Nlp;

@Named
@ViewScoped
public class NlpBean implements
    Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3214820947716487783L;

    private String input;

    private transient Nlp nlp;

    private AnalysisResult result;

    public String getInput() {

        return input;
    }

    public AnalysisResult getResult() {

        return result;
    }

    public boolean isParsed() {

        return result != null;
    }

    public void parse() {

        result = nlp.analyze(input);
    }

    public void setInput(final String input) {

        this.input = input;
    }

    //    @Inject
    public void setNlp(final Nlp nlp) {

        this.nlp = nlp;
    }
}
