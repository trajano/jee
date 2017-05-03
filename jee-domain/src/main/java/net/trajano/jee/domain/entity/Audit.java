package net.trajano.jee.domain.entity;

import javax.persistence.*;
import java.util.*;
import java.security.*;

@Embeddable
public class Audit {

    @Column(length = ColumnLengths.PRINCIPAL_LENGTH,
        updatable = false)
    private String createdBy;

    @Column(length = ColumnLengths.PRINCIPAL_LENGTH)
    private String lastUpdatedBy;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;

    public void updateAudit(Principal principal) {

        final Date current = new Date();
        if (this.createdBy == null) {
            this.createdBy = principal.getName();
            this.createdOn = current;
        }
        this.lastUpdatedBy = principal.getName();
        this.lastUpdatedOn = current;
    }
}
