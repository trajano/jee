package net.trajano.jee.domain.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.trajano.jee.domain.entity.Gender;

public class GenderConverterTest {

    @Test
    public void testGenderConverter() {

        final GenderConverter genderConverter = new GenderConverter();
        assertEquals("M", genderConverter.convertToDatabaseColumn(Gender.MALE));
        assertEquals(Gender.MALE, genderConverter.convertToEntityAttribute("M"));
    }
}
