package net.trajano.jee.domain.dao.impl;

public final class NamedQueries {

    /**
     * Counts by SIN but not for self. This is used for duplicate SIN checks.
     */
    public static final String PARTICIPANT_COUNT_BY_SIN_AND_NOT_SELF = "Participant.countBySinAndNotSelf";

    public static final String PARTICIPANT_GET_ALL = "Participant.getAll";

    public static final String PARTICIPANT_GET_BY_SIN = "Participant.getBySin";

    public static final String USER_COUNT_BY_USERNAME = "User.countByUserName";

    public static final String USER_GET_BY_USERNAME = "User.getByUserName";

    private NamedQueries() {

    }
}
