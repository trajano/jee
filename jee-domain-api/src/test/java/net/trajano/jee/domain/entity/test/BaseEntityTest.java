package net.trajano.jee.domain.entity.test;

import org.junit.Test;

import net.trajano.jee.domain.entity.Participant;

public class BaseEntityTest {

    /**
     * By design the audit fields are not visible on any API as such no
     * assertions can be made.
     */
    @Test
    public void testUpdateAudit() {

        final Participant p = new Participant();
        p.updateAudit("trajano");
        p.updateAudit("trajano");
    }
}
