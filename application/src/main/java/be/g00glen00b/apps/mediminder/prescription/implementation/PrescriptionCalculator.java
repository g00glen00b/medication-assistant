package be.g00glen00b.apps.mediminder.prescription.implementation;

import be.g00glen00b.apps.mediminder.availability.MedicationAvailabilityTotalDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.prescription.PrescriptionDTO;
import be.g00glen00b.apps.mediminder.utilities.BigDecimalUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class PrescriptionCalculator {
    private final Map<MedicationDTO, BigDecimal> requiredQuantities;
    private final Map<MedicationDTO, MedicationAvailabilityTotalDTO> availableQuantities;

    public List<PrescriptionDTO> calculate() {
        return streamRequiredMedications()
            .map(this::calculate)
            .flatMap(Optional::stream)
            .toList();
    }

    private Optional<PrescriptionDTO> calculate(MedicationDTO medication) {
        BigDecimal requiredQuantity = calculateRequiredQuantity(medication);
        if (!BigDecimalUtilities.isPositive(requiredQuantity)) {
            return Optional.empty();
        }
        MedicationAvailabilityTotalDTO availableQuantityTotal = calculateAvailableQuantityTotal(medication);
        if (availableQuantityTotal == null) {
            return Optional.of(new PrescriptionDTO(medication, requiredQuantity, BigDecimal.ZERO, requiredQuantity, null, null));
        }
        BigDecimal availableQuantity = availableQuantityTotal.totalQuantity();
        BigDecimal neededQuantity = requiredQuantity.subtract(availableQuantity);
        if (!BigDecimalUtilities.isPositive(neededQuantity)) {
            return Optional.empty();
        }
        BigDecimal initialQuantityPerItem = availableQuantityTotal.initialQuantityPerItem();
        if (initialQuantityPerItem == null) {
            return Optional.of(new PrescriptionDTO(medication, requiredQuantity, availableQuantity, neededQuantity, null, null));
        }
        BigDecimal neededSubscriptions = neededQuantity.divide(initialQuantityPerItem, RoundingMode.CEILING);
        return Optional.of(new PrescriptionDTO(medication, requiredQuantity, availableQuantity, neededQuantity, initialQuantityPerItem, neededSubscriptions));
    }

    private Stream<MedicationDTO> streamRequiredMedications() {
        return requiredQuantities.keySet().stream();
    }

    @NonNull
    private BigDecimal calculateRequiredQuantity(MedicationDTO medication) {
        return requiredQuantities.getOrDefault(medication, BigDecimal.ZERO);
    }

    @Nullable
    private MedicationAvailabilityTotalDTO calculateAvailableQuantityTotal(MedicationDTO medication) {
        return availableQuantities.get(medication);
    }

}
