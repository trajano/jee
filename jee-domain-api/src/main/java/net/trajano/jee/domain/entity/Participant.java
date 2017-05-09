package net.trajano.jee.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import net.trajano.jee.domain.constraint.CanadianSin;
import net.trajano.jee.domain.constraint.Email;
import net.trajano.jee.domain.converter.StaticEnumToStringConverter;

@Entity
@Table(indexes = @Index(columnList = "cancelled",
    unique = false))
public class Participant extends BaseEntity {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 9067331171225814003L;

    @Email
    @NotNull
    @Column(length = ColumnLengths.EMAIL,
        nullable = false)
    private String email;

    @NotNull
    @Column(length = ColumnLengths.ENUM,
        nullable = false)
    private String genderAtBirth;

    @Column(length = ColumnLengths.NAME,
        nullable = false)
    private String name;

    @NotNull
    @CanadianSin
    @Column(length = 9,
        nullable = false)
    private String sin;

    public String getEmail() {

        return email;
    }

    public Gender getGenderAtBirth() {

        return StaticEnumToStringConverter.convertToEntityAttribute(genderAtBirth, Gender.class);
    }

    public String getName() {

        return name;
    }

    public String getSin() {

        return sin;
    }

    public void setEmail(final String email) {

        this.email = email;
    }

    public void setGenderAtBirth(final Gender genderAtBirth) {

        this.genderAtBirth = StaticEnumToStringConverter.convertToDatabaseColumn(genderAtBirth);
    }

    public void setName(final String name) {

        this.name = name;
    }

    public void setSin(final String sin) {

        this.sin = sin;
    }

}
