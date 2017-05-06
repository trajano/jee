package net.trajano.jee.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Participant implements
    Auditable,
    Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 9067331171225814003L;

    @Embedded
    private final Audit audit = new Audit();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false,
        updatable = false)
    private Long id;

    @Column(length = ColumnLengths.NAME,
        nullable = false)
    private String name;

    @Version
    private long versionNo;

    @Override
    public Audit getAudit() {

        return audit;
    }

    public Long getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public void setName(final String name) {

        this.name = name;
    }

    @Override
    public String toString() {

        return "Participant [audit=" + audit + ", id=" + id + ", name=" + name + ", versionNo=" + versionNo + "]";
    }

}
