package net.trajano.jee.domain.entity;

public final class ColumnLengths {

    public static final int EMAIL = 128;

    /**
     * Length of enum strings.
     */
    public static final int ENUM = 10;

    public static final int NAME = 256;

    /**
     * Length of the string representing the Java Principal.
     */
    public static final int PRINCIPAL_LENGTH = 64;

    private ColumnLengths() {
    }
}
