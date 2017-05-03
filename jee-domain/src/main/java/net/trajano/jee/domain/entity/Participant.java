package net.trajano.jee.domain.entity;

import javax.persistence.*;

@Entity
public class Participant implements
    Auditable {

    @Id
    @GeneratedValue
    private long id;

    @Embedded
    private Audit audit;

    @Version
    private int versionNo;

    @Override
    public Audit getAudit() {

        return audit;
    }
}
