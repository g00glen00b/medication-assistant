package be.g00glen00b.apps.mediminder.prescription.implementation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

@Component
@RequiredArgsConstructor
public class DateWithinPeriodValidator implements ConstraintValidator<DateWithinPeriod, LocalDate> {
    private final Clock clock;
    private Period period = Period.ZERO;

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate finalDate = LocalDate.now(clock).plus(period);
        return !date.isAfter(finalDate);
    }

    @Override
    public void initialize(DateWithinPeriod constraintAnnotation) {
        this.period = Period.parse(constraintAnnotation.value());
    }
}
