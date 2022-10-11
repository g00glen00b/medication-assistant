package be.g00glen00b.apps.mediminder.medication.implementation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MedicationEntityTest {
    @Test
    void of_returnsEntity() {
        MedicationQuantityTypeEntity quantityType = MedicationQuantityTypeEntity.of("ml");
        MedicationEntity entity = MedicationEntity.of("Hydrocortisone", quantityType);
        assertThat(entity)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(new MedicationEntity(null, "Hydrocortisone", quantityType, null, null));
        assertThat(entity.getId()).isNotNull();
    }
}