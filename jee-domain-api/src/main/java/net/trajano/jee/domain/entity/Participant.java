package net.trajano.jee.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(columnList = "cancelled",
    unique = false))
public class Participant extends BaseEntity {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 9067331171225814003L;

    @Column(length = ColumnLengths.NAME,
        nullable = false)
    private String name;

    public String getName() {

        return name;
    }

    public void setName(final String name) {

        this.name = name;
    }

}
