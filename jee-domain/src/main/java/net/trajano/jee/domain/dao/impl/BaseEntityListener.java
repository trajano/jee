package net.trajano.jee.domain.dao.impl;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import net.trajano.jee.domain.entity.BaseEntity;

@ApplicationScoped
public class BaseEntityListener {

    /**
     * Injected session context.
     */
    private SessionContext sessionContext;

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
     * Updates the audit data for the entity. If the principal is not set (i.e.
     * during unit tests or through another means then it will use SYSTEM as the
     * user name.
     *
     * @param e
     *            entity
     */
    @PrePersist
    @PreUpdate
    void updateAudit(final BaseEntity e) {

        if (sessionContext == null) {
            e.updateAudit("SYSTEM");
        } else {
            e.updateAudit(sessionContext.getCallerPrincipal().getName());
        }
    }
}
