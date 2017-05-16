package net.trajano.jee.domain.dao.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.trajano.jee.domain.entity.ColumnLengths;

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
@IdClass(NameChunkSequence.class)
public class LobData {

    /**
     * 1 MB chunk size.
     */
    public static final int CHUNK_SIZE = 1024 * 1024;

    @Lob
    @Column(length = CHUNK_SIZE,
        nullable = false)
    private byte[] chunk;

    @Id
    @Column(nullable = false,
        updatable = false)
    private int chunkSequence;

    @Column(nullable = false,
        updatable = true)
    private boolean lastChunk;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedOn;

    @Id
    @Column(nullable = false,
        length = ColumnLengths.TECHNICAL_NAME,
        updatable = false)
    private String name;

    /**
     * Required for JPA do not use.
     */
    LobData() {
    }

}
