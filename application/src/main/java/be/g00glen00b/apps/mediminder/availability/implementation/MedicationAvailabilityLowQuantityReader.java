package be.g00glen00b.apps.mediminder.availability.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static be.g00glen00b.apps.mediminder.utilities.BigDecimalUtilities.toPercentage;

@RequiredArgsConstructor
public class MedicationAvailabilityLowQuantityReader extends RepositoryItemReader<LowMedicationAvailabilityInfo> {
    private final Clock clock;
    private final BigDecimal minimalPercentage;
    private final MedicationAvailabilityEntityRepository repository;
    private final int maximumItemCount;

    @BeforeStep
    public void beforeStep() {
        this.setRepository(repository);
        this.setSort(Map.of("medicationId", Sort.Direction.ASC));
        this.setMaxItemCount(maximumItemCount);
        this.setMethodName("findAllMedicationIdsWithQuantityPercentageLessThan");
        LocalDate today = LocalDate.now(clock);
        this.setArguments(List.of(toPercentage(minimalPercentage), today));
    }
}
