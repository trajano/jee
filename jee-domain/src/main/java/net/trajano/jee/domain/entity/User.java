package net.trajano.jee.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The physical table is named "USERS" to make it compatible with Oracle.
 *
 * @author Archimedes Trajano
 */
@Entity
@Table(name = "USERS")
public class User implements
    Auditable {

    @Embedded
    private Audit audit;

    @Id
    @GeneratedValue
    private long id;

    @Column(length = ColumnLengths.PRINCIPAL_LENGTH,
        unique = true)
    private String username;

    @Version
    private long versionNo;

    @Override
    public Audit getAudit() {

        return audit;
    }
}
