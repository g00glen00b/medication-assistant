package be.g00glen00b.apps.mediminder.schedule.implementation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Constraint(validatedBy = PositivePeriodValidator.class)
public @interface PositivePeriod {
    String message() default "{positivePeriod}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
