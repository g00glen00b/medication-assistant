package be.g00glen00b.apps.medicationassistant.core;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({TYPE, PARAMETER, FIELD, ANNOTATION_TYPE})
@Constraint(validatedBy = MinLocalDatePeriodValidator.class)
public @interface MinLocalDatePeriod {
    String message() default "{minLocalDatePeriod}";
    int value() default 1;
}
