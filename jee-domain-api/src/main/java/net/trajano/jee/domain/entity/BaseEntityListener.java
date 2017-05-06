package net.trajano.jee.domain.entity;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.enterprise.context.Dependent;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Dependent
public class BaseEntityListener {

    @Resource
    private transient SessionContext sessionContext;

    @PrePersist
    @PreUpdate
    public void updateAudit(final BaseEntity e) {

        e.updateAudit(sessionContext.getCallerPrincipal());
    }
}
