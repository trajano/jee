package net.trajano.jee.domain.entity;

import javax.persistence.*;
import java.util.*;

@Embeddable
public class Audit {

    @Column(length = ColumnLengths.PRINCIPAL_LENGTH)
    private String createdBy;

    @Column(length = ColumnLengths.PRINCIPAL_LENGTH)
    private String lastUpdatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;
}
