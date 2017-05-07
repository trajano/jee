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

    @Column(length = ColumnLengths.ENUM,
        nullable = false)
    private Gender genderAtBirth;

    @Column(length = ColumnLengths.NAME,
        nullable = false)
    private String name;

    public Gender getGenderAtBirth() {

        return genderAtBirth;
    }

    public String getName() {

        return name;
    }

    public void setGenderAtBirth(final Gender genderAtBirth) {

        this.genderAtBirth = genderAtBirth;
    }

    public void setName(final String name) {

        this.name = name;
    }

}
