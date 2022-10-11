package be.g00glen00b.apps.mediminder.availability.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MedicationAvailabilityEntityTest {
    @Test
    void of_createsEntityWithGivenFields() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDate expiryDate = LocalDate.of(2022, 10, 10);
        BigDecimal quantity = new BigDecimal("10");
        BigDecimal initialQuantity = new BigDecimal("100");
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);
        assertThat(entity)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(new MedicationAvailabilityEntity(null, medicationId, userId, quantity, initialQuantity, expiryDate, null, null));
        assertThat(entity.getId()).isNotNull();
    }

    @Test
    void of_setsInitialQuantityToQuantityIfNull() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDate expiryDate = LocalDate.of(2022, 10, 10);
        BigDecimal quantity = new BigDecimal("10");
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, null, expiryDate);
        assertThat(entity)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(new MedicationAvailabilityEntity(null, medicationId, userId, quantity, quantity, expiryDate, null, null));
    }

    @Test
    void isEmpty_isTrueIfQuantityIsZero() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal initialQuantity = new BigDecimal("100");
        LocalDate expiryDate = LocalDate.of(2022, 10, 10);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);
        assertThat(entity.isEmpty()).isTrue();
    }

    @Test
    void isEmpty_isTrueIfQuantityIsNegative() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("-1");
        BigDecimal initialQuantity = new BigDecimal("100");
        LocalDate expiryDate = LocalDate.of(2022, 10, 10);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);
        assertThat(entity.isEmpty()).isTrue();
    }

    @Test
    void isEmpty_isFalseIfQuantityIsPositive() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = BigDecimal.ONE;
        BigDecimal initialQuantity = new BigDecimal("100");
        LocalDate expiryDate = LocalDate.of(2022, 10, 10);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);
        assertThat(entity.isEmpty()).isFalse();
    }

    @Test
    void isContaining_isTrueIfQuantityCoversGivenAmount() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("2");
        BigDecimal initialQuantity = new BigDecimal("100");
        LocalDate expiryDate = LocalDate.of(2022, 10, 10);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);
        assertThat(entity.isContaining(new BigDecimal("1"))).isTrue();
    }

    @Test
    void isContaining_isFalseIfQuantityDoesNotCoverGivenAmount() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("2");
        BigDecimal initialQuantity = new BigDecimal("100");
        LocalDate expiryDate = LocalDate.of(2022, 10, 10);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);
        assertThat(entity.isContaining(new BigDecimal("10"))).isFalse();
    }

    @CsvSource({
        "2,0,8",
        "12,2,0",
        "0,0,10"
    })
    @ParameterizedTest
    void subtractQuantity(String subtractedQuantity, String resultQuantity, String remainderQuantity) {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("10");
        BigDecimal initialQuantity = new BigDecimal("100");
        LocalDate expiryDate = LocalDate.of(2022, 10, 10);
        MedicationAvailabilityEntity entity = MedicationAvailabilityEntity.of(medicationId, userId, quantity, initialQuantity, expiryDate);
        BigDecimal result = entity.subtractQuantity(new BigDecimal(subtractedQuantity));
        assertThat(result).isEqualByComparingTo(resultQuantity);
        assertThat(entity.getQuantity()).isEqualByComparingTo(remainderQuantity);
    }
}