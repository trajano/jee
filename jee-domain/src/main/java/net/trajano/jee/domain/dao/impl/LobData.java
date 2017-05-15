package net.trajano.jee.domain.dao.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This entity stores keyed LOB data that is meant to be used by system services
 * rather than by general user interaction. This entity is not exposed to
 * clients as it only has the lob data so all interaction is through the DAO.
 * <p>
 * This entity is not normally used either as raw JDBC calls are used to improve
 * performance.
 *
 * @author Archimedes Trajano
 */
@Entity
public class LobData {

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

    @Lob
    @Column(length = Integer.MAX_VALUE,
        nullable = false)
    private byte[] lobData;

    /**
     * Required for JPA do not use.
     */
    LobData() {
    }

    /**
     * Constructs the lob data with the ID provided.
     *
     * @param id
     *            ID
     * @param lobData
     *            data
     */
    public LobData(final long id,
        final byte[] lobData) {
        this.id = id;
        this.lobData = lobData;
    }

    public byte[] getLobData() {

        return lobData;
    }

    public void setLobData(final byte[] lobData) {

        this.lobData = lobData;
    }

    @PrePersist
    @PreUpdate
    public void updateLastUpdateOn() {

        lastUpdatedOn = new Date();
    }

}
