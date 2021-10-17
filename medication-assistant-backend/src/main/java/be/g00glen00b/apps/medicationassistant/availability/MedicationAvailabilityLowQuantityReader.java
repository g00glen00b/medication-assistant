package be.g00glen00b.apps.medicationassistant.availability;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.data.RepositoryItemReader;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
class MedicationAvailabilityLowQuantityReader extends RepositoryItemReader<MedicationAvailabilityPercentage> {
    private final MedicationAvailabilityRepository repository;
    private final BigDecimal percentage;

    @BeforeStep
    public void initializeDateToCheck() {
        this.setRepository(repository);
        this.setMethodName("findAllByPercentageAvailable");
        this.setArguments(List.of(percentage));
        this.setSort(Map.of());
        this.setPageSize(100);
    }
}
