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
