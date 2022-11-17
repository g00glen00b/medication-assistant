package be.g00glen00b.apps.mediminder.prescription.implementation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Constraint(validatedBy = DateWithinPeriodValidator.class)
public @interface DateWithinPeriod {
    String value() default "P0D";
    String message() default "{dateWithinPeriod}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
