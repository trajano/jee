package net.trajano.jee.domain.constraint.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.trajano.jee.domain.constraint.CanadianPostalCode;
import net.trajano.jee.domain.constraint.CanadianPostalCodeValidator;

public class PostalCodeTest {

    @Test
    @CanadianPostalCode
    public void testInvalidPostalCodes() throws Exception {

        final CanadianPostalCodeValidator validator = new CanadianPostalCodeValidator();
        validator.initialize(getClass().getMethod("testInvalidPostalCodes").getAnnotation(CanadianPostalCode.class));
        final String[] valid = {
            "W1T 1G5",
            "   M1T 1G5   ",
            "   m1t1g5   ",
            "   m1t 1g5   ",
            "   M1T 1G5   ",
            "archie",
            "",
        };
        for (final String input : valid) {
            assertFalse(input + " should be invalid", validator.isValid(input, null));
        }
    }

    @Test
    @CanadianPostalCode
    public void testValidPostalCodes() throws Exception {

        final CanadianPostalCodeValidator validator = new CanadianPostalCodeValidator();
        validator.initialize(getClass().getMethod("testValidPostalCodes").getAnnotation(CanadianPostalCode.class));
        final String[] valid = {
            "M1T 1G5",
            "A1T 1G5"
        };
        for (final String input : valid) {
            assertTrue(input + " should be valid", validator.isValid(input, null));
        }
    }

    @Test
    @CanadianPostalCode(relaxed = true)
    public void testValidPostalCodesRelaxed() throws Exception {

        final CanadianPostalCodeValidator validator = new CanadianPostalCodeValidator();
        validator.initialize(getClass().getMethod("testValidPostalCodesRelaxed").getAnnotation(CanadianPostalCode.class));
        final String[] valid = {
            "M1T 1G5",
            "   M1T 1G5   ",
            "   m1t1g5   ",
            "   m1t 1g5   ",
            "   M1T 1G5   ",
            "A1T 1G5"
        };
        for (final String input : valid) {
            assertTrue(input + " should be valid", validator.isValid(input, null));
        }
    }

}
