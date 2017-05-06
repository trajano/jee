package net.trajano.jee.domain.entity;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.enterprise.context.Dependent;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Dependent
public class BaseEntityListener {

    /**
     * Injected session context.
     */
    private transient SessionContext sessionContext;

    /**
     * Sets/injects the session context.
     *
     * @param sessionContext
     *            session context.
     */
    @Resource
    public void setSessionContext(final SessionContext sessionContext) {

        this.sessionContext = sessionContext;
    }

    /**
     * Updates the audit data for the entity.
     *
     * @param e
     *            entity
     */
    @PrePersist
    @PreUpdate
    void updateAudit(final BaseEntity e) {

        e.updateAudit(sessionContext.getCallerPrincipal());
    }
}
