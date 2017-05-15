package net.trajano.jee.nlp.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TensorGraph {

    @Lob
    @Column(length = Integer.MAX_VALUE,
        nullable = false)
    private byte[] graphDef;

    /**
     * Primary key. These are internal IDs and are not exposed to the user. The
     * ID will not be generated, instead it is actually set by the constructor
     * for new objects.
     */
    @Id
    @Column(nullable = false,
        updatable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedOn;

    /**
     * Constructs TensorGraph. Required for JPA do not use.
     */
    TensorGraph() {
    }

    public TensorGraph(final long id) {
        this.id = id;
    }

    public byte[] getGraphDef() {

        return graphDef;
    }

    public void setGraphDef(final byte[] graph) {

        graphDef = graph;
    }

    @PrePersist
    @PreUpdate
    public void updateLastUpdateOn() {

        lastUpdatedOn = new Date();
    }

}
