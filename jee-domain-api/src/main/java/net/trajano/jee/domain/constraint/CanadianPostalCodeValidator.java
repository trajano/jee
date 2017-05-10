package net.trajano.jee.domain.constraint;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CanadianPostalCodeValidator implements
    ConstraintValidator<CanadianPostalCode, String> {

    private Pattern pattern;

    @Override
    public void initialize(final CanadianPostalCode constraintAnnotation) {

        if (constraintAnnotation.relaxed()) {
            pattern = Pattern.compile("\\s*[ABCEGHJ-NPRSTVXY][0-9][ABCEGHJ-NPRSTV-Z]\\s*[0-9][ABCEGHJ-NPRSTV-Z][0-9]\\s*", Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile("[ABCEGHJ-NPRSTVXY][0-9][ABCEGHJ-NPRSTV-Z] [0-9][ABCEGHJ-NPRSTV-Z][0-9]");
        }
    }

    @Override
    public boolean isValid(final String value,
        final ConstraintValidatorContext context) {

        return pattern.matcher(value).matches();
    }

}
