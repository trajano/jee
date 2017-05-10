package net.trajano.jee.domain.dao.test;

import org.junit.Test;

import net.trajano.jee.domain.dao.impl.StartupBean;

public class StartupBeanTest extends BaseJpaTest {

    @Test
    public void testBean() {

        final StartupBean bean = new StartupBean();
        bean.setEntityManager(em);
        bean.ensureDatabase();
    }

}
