package net.trajano.jee.domain.dao.impl;

import java.security.Principal;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseDAO {

    protected EntityManager em;

    private SessionContext sctx;

    protected Principal getPrincipal() {

        return sctx.getCallerPrincipal();
    }

    @PersistenceContext
    public void setEntityManager(final EntityManager em) {

        this.em = em;
    }

    @Resource
    public void setSessionContext(final SessionContext sctx) {

        this.sctx = sctx;
    }

}
