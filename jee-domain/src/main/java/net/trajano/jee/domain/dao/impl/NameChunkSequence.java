package net.trajano.jee.domain.dao.impl;

import java.io.Serializable;

public class NameChunkSequence implements
    Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -1316941647871366824L;

    private int chunkSequence;

    private String name;

    public int getChunkSequence() {

        return chunkSequence;
    }

    public String getName() {

        return name;
    }

    public void setChunkSequence(final int chunkSequence) {

        this.chunkSequence = chunkSequence;
    }

    public void setName(final String name) {

        this.name = name;
    }
}
