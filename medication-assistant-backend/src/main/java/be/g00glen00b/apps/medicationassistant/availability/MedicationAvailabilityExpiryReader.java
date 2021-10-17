package be.g00glen00b.apps.medicationassistant.availability;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.data.RepositoryItemReader;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
class MedicationAvailabilityExpiryReader extends RepositoryItemReader<MedicationAvailability> {
    private final Clock clock;
    private final Period period;
    private final MedicationAvailabilityRepository repository;

    @BeforeStep
    public void initializeDateToCheck() {
        this.setRepository(repository);
        this.setMethodName("findAllByExpiryDateLessThan");
        this.setArguments(List.of(LocalDate.now(clock).plus(period)));
        this.setSort(Map.of());
        this.setPageSize(100);
    }
}
