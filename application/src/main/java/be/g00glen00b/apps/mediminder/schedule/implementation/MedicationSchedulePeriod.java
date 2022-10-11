package be.g00glen00b.apps.mediminder.schedule.implementation;

import be.g00glen00b.apps.mediminder.schedule.InvalidMedicationScheduleException;
import lombok.*;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MedicationSchedulePeriod {
    private LocalDate startingAt;
    private LocalDate endingAtInclusive;

    public static MedicationSchedulePeriod ofEndInclusive(LocalDate startingAt, LocalDate endingAtInclusive) {
        if (endingAtInclusive != null && endingAtInclusive.isBefore(startingAt)) {
            throw new InvalidMedicationScheduleException("End date has to go after the start date when given");
        }
        return new MedicationSchedulePeriod(startingAt, endingAtInclusive);
    }

    public static MedicationSchedulePeriod ofUnboundedEnd(LocalDate startingAt) {
        return ofEndInclusive(startingAt, null);
    }
}
