package be.g00glen00b.apps.mediminder.schedule.implementation;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Period;

@Component
public class PositivePeriodValidator implements ConstraintValidator<PositivePeriod, Period> {
    @Override
    public boolean isValid(Period value, ConstraintValidatorContext context) {
        return value == null || (!value.isNegative() && !value.isZero());
    }
}
