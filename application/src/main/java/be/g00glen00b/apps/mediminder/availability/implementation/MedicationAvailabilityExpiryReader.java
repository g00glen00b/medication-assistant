package be.g00glen00b.apps.mediminder.availability.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class MedicationAvailabilityExpiryReader extends RepositoryItemReader<MedicationAvailabilityEntity> {
    private final Clock clock;
    private final Period subtractedPeriod;
    private final MedicationAvailabilityEntityRepository repository;
    private final int maximumItemCount;

    @BeforeStep
    public void beforeStep() {
        this.setRepository(repository);
        this.setSort(Map.of("id", Sort.Direction.ASC));
        this.setMaxItemCount(maximumItemCount);
        this.setMethodName("findAllExpiredBeforeDate");
        this.setArguments(List.of(LocalDate.now(clock).plus(subtractedPeriod)));
    }
}
