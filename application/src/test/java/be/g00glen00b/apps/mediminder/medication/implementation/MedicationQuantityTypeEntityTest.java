package be.g00glen00b.apps.mediminder.medication.implementation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MedicationQuantityTypeEntityTest {
    @Test
    void of_returnsEntity() {
        MedicationQuantityTypeEntity entity = MedicationQuantityTypeEntity.of("ml");
        assertThat(entity.getName()).isEqualTo("ml");
        assertThat(entity.getId()).isNotNull();
        assertThat(entity.getCreatedDate()).isNull();
        assertThat(entity.getLastModifiedDate()).isNull();
    }
}