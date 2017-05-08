package net.trajano.jee.domain.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = CanadianSinValidator.class)
@Documented
@Target({
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface CanadianSin {

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

        CanadianSin[] value();
    }

    Class<?>[] groups() default {};

    String message() default "{invalid Canadian SIN}";

    Class<? extends Payload>[] payload() default {};

    public boolean stripSpacesAndSymbols() default false;
}
