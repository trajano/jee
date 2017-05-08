package net.trajano.jee.domain.converter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.trajano.jee.domain.constraint.CanadianSin;
import net.trajano.jee.domain.constraint.CanadianSinValidator;

public class SinTest {

    @Test
    @CanadianSin(stripSpacesAndSymbols = true)
    public void testFailSinSkipSymbols() throws Exception {

        final CanadianSinValidator validator = new CanadianSinValidator();
        validator.initialize(getClass().getMethod("testSinSkipSymbols").getAnnotation(CanadianSin.class));
        assertFalse(validator.isValid("349 798 053", null));
        assertFalse(validator.isValid("349 798 05A", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGeneratedBadFirstDigit1() {

        CanadianSinValidator.generate(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGeneratedBadFirstDigit2() {

        CanadianSinValidator.generate(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGeneratedBadFirstDigit3() {

        CanadianSinValidator.generate(8);
    }

    /**
     * Test generation of 10000 SINs.
     */
    @Test
    public void testGeneratedSin() {

        final CanadianSinValidator validator = new CanadianSinValidator();
        for (int i = 0; i < 10000; ++i) {
            final String generate = CanadianSinValidator.generate();
            assertTrue(generate + " is not a valid SIN", validator.isValid(generate, null));
        }
    }

    /**
     * Test generation of many SINs for non-zero first digit.
     */
    @Test
    public void testGeneratedSinNonZeroFirstDigit() {

        final CanadianSinValidator validator = new CanadianSinValidator();
        final int[] firstDigits = {
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            9
        };
        for (int i = 0; i < 10000; ++i) {
            for (final int firstDigit : firstDigits) {
                final String generate = CanadianSinValidator.generate(firstDigit);
                assertTrue(generate + " is not a valid SIN", validator.isValid(generate, null));
            }
        }
    }

    /**
     * Use numbers that are valid according to LUGN but not according to
     * Canadian SIN rules..
     */
    @Test
    public void testInvalidSin8Digits() {

        final CanadianSinValidator validator = new CanadianSinValidator();
        assertFalse(validator.isValid("3497980338", null));
        assertFalse(validator.isValid("34979807", null));
        assertFalse(validator.isValid("3497989", null));
    }

    @Test
    public void testSin() {

        final CanadianSinValidator validator = new CanadianSinValidator();
        assertTrue(validator.isValid("349798033", null));
    }

    @Test
    @CanadianSin(stripSpacesAndSymbols = true)
    public void testSinSkipSymbols() throws Exception {

        final CanadianSinValidator validator = new CanadianSinValidator();
        validator.initialize(getClass().getMethod("testSinSkipSymbols").getAnnotation(CanadianSin.class));
        assertTrue(validator.isValid("349 798 033", null));
        assertTrue(validator.isValid("    349 798 033   ", null));
        assertTrue(validator.isValid("    349-798-033   ", null));
        assertTrue(validator.isValid("    349 798 03   3", null));
    }

    /**
     * A SIN that starts with 8 is not a valid SIN according to
     * https://en.wikipedia.org/wiki/Social_Insurance_Number#Geography.
     */
    @Test
    public void testStartingWith8() {

        final CanadianSinValidator validator = new CanadianSinValidator();
        assertFalse(validator.isValid("834979890", null));
    }
}
