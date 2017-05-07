package net.trajano.jee.domain.entity;

public enum Gender {
    FEMALE("F"),
    MALE("M"),
    NOT_DISCLOSED("ND");

    private String s;

    Gender(final String s) {
        this.s = s;
    }

    @Override
    public String toString() {

        return s;
    }
}
