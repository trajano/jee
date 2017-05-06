package net.trajano.jee.domain.entity;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class Audit implements
    Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -7662233764407179488L;

    /**
     * Indicates that the record has been cancelled and should not be considered
     * in queries.
     */
    @Column(nullable = false)
    private boolean cancelled = false;

    @Column(length = ColumnLengths.PRINCIPAL_LENGTH,
        updatable = false)
    private String createdBy;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(length = ColumnLengths.PRINCIPAL_LENGTH)
    private String lastUpdatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;

    /**
     * Flags that the record as cancelled and should not be considered in
     * queries.
     */
    public void cancel() {

        cancelled = true;
    }

    public void updateAudit(final Principal principal) {

        final Date current = new Date();
        if (createdBy == null) {
            createdBy = principal.getName();
            createdOn = current;
        }
        lastUpdatedBy = principal.getName();
        lastUpdatedOn = current;
    }

}
