package be.g00glen00b.apps.mediminder.medication.implementation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:tc:postgresql:14.5-alpine3.16:///mediminder"
})
class MedicationEntityRepositoryTest {
    private static final UUID MEDICATION_1_ID = UUID.fromString("ed8c8540-b080-4832-a663-ff8d2c195ae9");
    private static final UUID MEDICATION_2_ID = UUID.fromString("72ae4f14-08fe-43ea-8b65-a8a87ccdc6ef");
    @Autowired
    private MedicationEntityRepository repository;

    @Test
    @Sql("classpath:medication.sql")
    void findByNameIgnoreCase_returnsExactResults() {
        Optional<MedicationEntity> result = repository.findByNameIgnoreCase("hydrocortisone 14mg");
        assertThat(result)
            .isNotEmpty()
            .get()
            .extracting(MedicationEntity::getId)
            .isEqualTo(MEDICATION_1_ID);
    }

    @Test
    @Sql("classpath:medication.sql")
    void findByNameIgnoreCase_returnsNothingIfNameDoesntMatch() {
        Optional<MedicationEntity> result = repository.findByNameIgnoreCase("hydro");
        assertThat(result).isEmpty();
    }

    @Test
    @Sql("classpath:medication.sql")
    void findAllByNameContainingIgnoreCase() {
        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<MedicationEntity> results = repository.findAllByNameContainingIgnoreCase("hydro", pageRequest);
        assertThat(results)
            .hasSize(2)
            .extracting(MedicationEntity::getId)
            .containsOnly(MEDICATION_1_ID, MEDICATION_2_ID);
    }

    @Test
    @Sql("classpath:medication.sql")
    void findAllByNameContainingIgnoreCase_returnsOnlyMatchingName() {
        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<MedicationEntity> results = repository.findAllByNameContainingIgnoreCase("8mg", pageRequest);
        assertThat(results)
            .hasSize(1)
            .extracting(MedicationEntity::getId)
            .containsOnly(MEDICATION_2_ID);
    }
}