package net.trajano.jee.domain.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeConverter;

abstract class EnumToStringConverter<E extends Enum<E>> implements
    AttributeConverter<E, String> {

    private final Map<String, E> stringToEnumMap;

    protected EnumToStringConverter(final Class<E> enumClass) {
        final Map<String, E> tempMap = new HashMap<>();
        final E[] enumConstants = enumClass.getEnumConstants();
        for (final E c : enumConstants) {
            tempMap.put(c.toString(), c);
        }
        this.stringToEnumMap = Collections.unmodifiableMap(tempMap);
    }

    @Override
    public final String convertToDatabaseColumn(final E attribute) {

        if (attribute == null) {
            return null;
        }
        return attribute.toString();
    }

    @Override
    public final E convertToEntityAttribute(final String dbData) {

        if (dbData == null) {
            return null;
        }
        return stringToEnumMap.get(dbData);
    }
}
