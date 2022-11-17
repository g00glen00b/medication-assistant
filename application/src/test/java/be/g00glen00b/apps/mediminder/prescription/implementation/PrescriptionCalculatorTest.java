package be.g00glen00b.apps.mediminder.prescription.implementation;

import be.g00glen00b.apps.mediminder.availability.MedicationAvailabilityTotalDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationQuantityTypeDTO;
import be.g00glen00b.apps.mediminder.prescription.PrescriptionDTO;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PrescriptionCalculatorTest {
    @Test
    void calculate_calculatesPrescriptions() {
        MedicationDTO medication1 = aMedication();
        MedicationDTO medication2 = aMedication();
        MedicationDTO medication3 = aMedication();
        MedicationDTO medication4 = aMedication();
        Map<MedicationDTO, BigDecimal> requiredQuantities = Map.of(
            medication1, new BigDecimal(10),
            medication2, new BigDecimal(5),
            medication3, new BigDecimal(2),
            medication4, new BigDecimal(3)
        );
        Map<MedicationDTO, MedicationAvailabilityTotalDTO> availableQuantities = Map.of(
            medication1, new MedicationAvailabilityTotalDTO(medication1, new BigDecimal(2), new BigDecimal(4)),
            medication3, new MedicationAvailabilityTotalDTO(medication3, new BigDecimal(2), new BigDecimal(2)),
            medication4, new MedicationAvailabilityTotalDTO(medication4, null, new BigDecimal(2))
        );
        PrescriptionCalculator calculator = new PrescriptionCalculator(requiredQuantities, availableQuantities);
        assertThat(calculator.calculate())
            .hasSize(3)
            .contains(
                new PrescriptionDTO(
                medication1,
                new BigDecimal(10),
                new BigDecimal(4),
                new BigDecimal(6),
                new BigDecimal(2),
                new BigDecimal(3)
            ))
            .contains(new PrescriptionDTO(
                medication2,
                new BigDecimal(5),
                BigDecimal.ZERO,
                new BigDecimal(5),
                null,
                null
            ))
            .contains(new PrescriptionDTO(
                medication4,
                new BigDecimal(3),
                new BigDecimal(2),
                new BigDecimal(1),
                null,
                null
            ));
    }

    @NotNull
    private static MedicationDTO aMedication() {
        UUID medicationId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "capsules");
        return new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
    }
}