package net.trajano.jee.bean;

import javax.inject.Named;

@Named
public class EditorBean {

    private String value = "This editor is provided by PrimeFaces";

    public String getValue() {

        return value;
    }

    public void setValue(final String value) {

        this.value = value;
    }
}
