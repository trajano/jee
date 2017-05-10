package net.trajano.jee.domain.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = CanadianPostalCodeValidator.class)
@Documented
@Target({
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface CanadianPostalCode {

    @Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER
    })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {

        CanadianPostalCode[] value();
    }

    Class<?>[] groups() default {};

    String message() default "{invalid Canadian Postal code}";

    Class<? extends Payload>[] payload() default {};

    /**
     * If this is <code>true</code>, then extra spaces and lower case values are
     * allowed.
     *
     * @return relaxed check
     */
    public boolean relaxed() default false;
}
