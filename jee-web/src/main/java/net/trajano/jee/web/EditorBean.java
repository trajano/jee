package net.trajano.jee.web;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "editor")
public class EditorBean {

    private String value = "This editor is provided by PrimeFaces";

    public String getValue() {

        return value;
    }

    public void setValue(final String value) {

        this.value = value;
    }
}
