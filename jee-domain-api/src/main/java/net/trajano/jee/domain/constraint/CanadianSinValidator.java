package net.trajano.jee.domain.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CanadianSinValidator implements
    ConstraintValidator<CanadianSin, String> {

    private static final int[] MAP = {
        0,
        2,
        4,
        6,
        8,
        1 + 0,
        1 + 2,
        1 + 4,
        1 + 6,
        1 + 8
    };

    private boolean stripSpacesAndSymbols;

    @Override
    public void initialize(final CanadianSin constraintAnnotation) {

        stripSpacesAndSymbols = constraintAnnotation.stripSpacesAndSymbols();
    }

    @Override
    public boolean isValid(final String value,
        final ConstraintValidatorContext context) {

        int di = 0;
        int checksum = 0;
        for (final char c : value.toCharArray()) {
            if (stripSpacesAndSymbols && (c == ' ' || c == '-')) {
                continue;
            }
            ++di;
            final int digit = c - '0';
            if (di > 9 || digit > 9 || di == 1 && digit == 8) {
                return false;
            } else if (di % 2 == 1) {
                checksum += digit;
            } else {
                checksum += MAP[digit];
            }
        }
        return di == 9 && checksum % 10 == 0;
    }

}
