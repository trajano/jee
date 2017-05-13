package net.trajano.jee.domain.converter;

import java.util.EnumSet;

/**
 * JPA Converters do not work properly yet. Waiting on
 * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=443546">Bug 443546</a>
 * to be patched on WebSphere.
 *
 * @author Archimedes Trajano
 */
public final class StaticEnumToStringConverter {

    public static <E extends Enum<E>> String convertToDatabaseColumn(final E attribute) {

        if (attribute == null) {
            return null;
        }
        return attribute.toString();
    }

    public static <E extends Enum<E>> E convertToEntityAttribute(final String dbData,
        final Class<E> enumClass) {

        if (dbData == null) {
            return null;
        }
        for (final E c : EnumSet.allOf(enumClass)) {
            if (dbData.equals(c.toString())) {
                return c;
            }
        }
        throw new IllegalArgumentException(dbData);

    }

    private StaticEnumToStringConverter() {

    }
}
