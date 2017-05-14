package net.trajano.jee.nlp;

import javax.ejb.Local;

@Local
public interface Nlp {

    AnalysisResult analyze(String text);
}
