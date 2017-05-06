package net.trajano.jee.domain.dao.impl;

import java.security.Principal;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

public class BaseDAO {

    @Resource(name = "java:comp/env/jdbc/jee")
    private DataSource ds;

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
