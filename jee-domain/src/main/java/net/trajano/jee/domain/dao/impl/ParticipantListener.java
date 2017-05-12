package net.trajano.jee.domain.dao.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.TypedQuery;

import net.trajano.jee.domain.dao.DuplicateSinException;
import net.trajano.jee.domain.entity.Participant;

@ApplicationScoped
public class ParticipantListener {

    @Inject
    private EntityManager em;

    /**
     * Updates are only allowed when the SIN is unique among active records.
     *
     * @param e
     *            participant
     */
    @PrePersist
    @PreUpdate
    void ensureNoDuplicateSin(final Participant e) {

        // Perform the query using em rather than DAO to set the flush mode.
        final TypedQuery<Long> q = em.createNamedQuery(NamedQueries.PARTICIPANT_COUNT_BY_SIN_AND_NOT_SELF, Long.class);
        q.setFlushMode(FlushModeType.COMMIT);
        q.setParameter("sin", e.getSin());
        q.setParameter("self", e);

        if (q.getSingleResult() != 0L) {
            throw new DuplicateSinException();
        }
    }

}
