package net.trajano.jee.domain.dao.impl;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.sql.DataSource;

@Singleton
@Startup
public class StartupBean {

    @Resource(name = "java:app/ds")
    private DataSource ds;
}
