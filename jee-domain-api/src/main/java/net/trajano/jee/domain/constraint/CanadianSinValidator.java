package net.trajano.jee.domain.constraint;

import java.util.Random;

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

    /**
     * This generates a fake SIN. Fake SINs used by CRA start with 0.
     *
     * @return a randomly generated fake SIN.
     */
    public static String generate() {

        int checksum = 0;
        // Choose middle digits such that there are 7 sigits, but the first digit may not be 0 or 9
        // the reason the first digit cannot be 9 is to ensure there's room for a carry when the checksum mod 10 is 0
        final int middleDigits = new Random().nextInt(9999999 + 1 - 1000000 - 1000000) + 1000000;
        final int[] digits = new int[] {
            middleDigits / 1000000 % 10,
            middleDigits / 100000 % 10,
            middleDigits / 10000 % 10,
            middleDigits / 1000 % 10,
            middleDigits / 100 % 10,
            middleDigits / 10 % 10,
            middleDigits % 10
        };
        for (int i = 0; i < 7; ++i) {
            if (i % 2 == 1) {
                checksum += digits[i];
            } else {
                checksum += MAP[digits[i]];
            }
        }
        while (checksum % 10 == 0) {
            checksum = checksum - MAP[digits[0]] - digits[1];
            digits[0] = (digits[1] + 1) / 10;
            digits[1] = (digits[1] + 1) % 10;
            checksum = checksum + MAP[digits[0]] + digits[1];
        }
        final int checkDigit = 10 - checksum % 10;
        return "0" + digits[0] + digits[1] + digits[2] + digits[3] + digits[4] + digits[5] + digits[6] + checkDigit;
    }

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
