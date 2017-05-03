package net.trajano.jee.domain.entity;

import javax.persistence.*;

@Entity
public class Participant {

    @Id
    @GeneratedValue
    private long id;

    @Embedded
    private Audit audit;
}
