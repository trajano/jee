package net.trajano.jee.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The physical table is named "USERS" to make it compatible with Oracle.
 *
 * @author Archimedes Trajano
 */
@Entity
@Table(name = "USERS")
public class User extends BaseEntity {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -7953114178733155155L;

    @Column(length = ColumnLengths.PRINCIPAL_LENGTH,
        unique = true,
        nullable = false)
    private String username;

    User() {
    }

    public User(final String username) {
        this.username = username;
    }
}
