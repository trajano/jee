package net.trajano.jee.domain.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Participant implements
    Auditable {

    @Embedded
    private Audit audit;

    @Id
    @GeneratedValue
    private long id;

    @Version
    private long versionNo;

    @Override
    public Audit getAudit() {

        return audit;
    }
}
