package net.trajano.jee.domain.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.trajano.jee.domain.entity.Gender;

public class GenderConverterTest {

    @Test
    public void testGenderConverter() {

        final GenderConverter genderConverter = new GenderConverter();
        assertEquals("M", genderConverter.convertToDatabaseColumn(Gender.MALE));
        assertEquals(null, genderConverter.convertToDatabaseColumn(null));
        assertEquals(Gender.MALE, genderConverter.convertToEntityAttribute("M"));
        assertEquals(null, genderConverter.convertToEntityAttribute(null));
    }

    @Test
    public void testStaticConverter() {

        assertEquals("F", StaticEnumToStringConverter.convertToDatabaseColumn(Gender.FEMALE));
        assertEquals(Gender.FEMALE, StaticEnumToStringConverter.convertToEntityAttribute("F", Gender.class));
        assertEquals(null, StaticEnumToStringConverter.convertToDatabaseColumn(null));
        assertEquals(null, StaticEnumToStringConverter.convertToEntityAttribute(null, Gender.class));
    }
}
