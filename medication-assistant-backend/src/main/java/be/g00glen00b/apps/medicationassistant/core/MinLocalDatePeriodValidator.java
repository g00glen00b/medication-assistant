package be.g00glen00b.apps.medicationassistant.core;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

@Component
public class MinLocalDatePeriodValidator implements ConstraintValidator<MinLocalDatePeriod, LocalDatePeriod> {
    private int validNumberOfDays = 1;

    @Override
    public boolean isValid(LocalDatePeriod localDatePeriod, ConstraintValidatorContext context) {
        LocalDate endDateInclusive = localDatePeriod.getEndingAt().plusDays(1);
        Period period = Period.between(localDatePeriod.getStartingAt(), endDateInclusive);
        return period.getDays() >= this.validNumberOfDays;
    }

    @Override
    public void initialize(MinLocalDatePeriod constraintAnnotation) {
        this.validNumberOfDays = constraintAnnotation.value();
    }
}
