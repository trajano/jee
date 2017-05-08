package net.trajano.jee.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * This provides common data and functionality that is used across all entities.
 * They are primarily for primary keys, optimistic locking, and auditing. This
 * should not be used for entities that cannot stand alone.
 * <p>
 * Entities deriving from this class
 *
 * @author Archimedes Trajano
 */
@MappedSuperclass
@EntityListeners(BaseEntityListener.class)
public abstract class BaseEntity implements
    Serializable {

    /**
     * serialVersionUID. Since these entities would likely be reused in JSF and
     * may need to be serialized.
     */
    private static final long serialVersionUID = -7662233764407179488L;

    /**
     * Indicates that the record has been cancelled and should not be considered
     * in queries.
     */
    @Column(nullable = false)
    private boolean cancelled = false;

    /**
     * User that initially created the record.
     */
    @Column(length = ColumnLengths.PRINCIPAL_LENGTH,
        updatable = false)
    private String createdBy;

    /**
     * When the record was initially created.
     */
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    /**
     * Primary key. These are internal IDs and are not exposed to the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false,
        updatable = false)
    private Long id;

    /**
     * User that last updated the record.
     */
    @Column(length = ColumnLengths.PRINCIPAL_LENGTH)
    private String lastUpdatedBy;

    /**
     * When the record was last modified. Creation will also update this record.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;

    /**
     * Optimistic locking version.
     */
    @Version
    private long versionNo;

    /**
     * Flags that the record as cancelled and should not be considered in
     * queries.
     */
    public void cancel() {

        cancelled = true;
    }

    /**
     * Returns the primary key. May be <code>null</code> if not
     * {@link #isAssigned()}.
     *
     * @return primary key.
     */
    public Long getId() {

        return id;
    }

    /**
     * Retruens the optimistic locking version number.
     *
     * @return
     */
    public long getVersionNo() {

        return versionNo;
    }

    /**
     * Checks if the entity is assigned with a primary key.
     *
     * @return primary key is not {@code null}
     */
    public boolean isAssigned() {

        return id != null;
    }

    /**
     * This sets the key data for updating the record in the database later.
     * These are normally set together as such ther is no individual setter for
     * ID and versionNo.
     * 
     * @param id
     *            primary key
     * @param versionNo
     *            optimistic locking version
     */
    public void setUpdateKey(final long id,
        final long versionNo) {

        this.id = id;
        this.versionNo = versionNo;
    }

    /**
     * Updates the audit data. This is called by {@link BaseEntityListener}
     *
     * @param username
     *            user name
     */
    void updateAudit(final String username) {

        final Date current = new Date();
        if (createdBy == null) {
            createdBy = username;
            createdOn = current;
        }
        lastUpdatedBy = username;
        lastUpdatedOn = current;
    }
}
