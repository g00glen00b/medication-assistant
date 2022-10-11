package be.g00glen00b.apps.mediminder.availability.implementation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:tc:postgresql:14.5-alpine3.16:///mediminder"
})
class MedicationAvailabilityEntityRepositoryTest {
    private static final UUID MEDICATION_1_ID = UUID.fromString("ed8c8540-b080-4832-a663-ff8d2c195ae9");
    private static final UUID MEDICATION_2_ID = UUID.fromString("72ae4f14-08fe-43ea-8b65-a8a87ccdc6ef");
    private static final UUID USER_ID = UUID.fromString("0a808223-9fd3-425e-9953-bbadbf44a79d");
    private static final UUID AVAILABILITY_1_ID = UUID.fromString("e38e146f-a13c-4c54-a02a-b4547126f1f6");
    private static final UUID AVAILABILITY_2_ID = UUID.fromString("0d3b47e8-c2b1-48e9-954c-b4e98446ad97");
    @Autowired
    private MedicationAvailabilityEntityRepository repository;

    @Test
    @Sql("classpath:availabilities.sql")
    void findAllNonEmptyNonExpiredByUserId_returnsNonExpiredNonEmptyResults() {
        LocalDate date = LocalDate.of(2022, 10, 10);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MedicationAvailabilityEntity> results = repository.findAllNonEmptyNonExpiredByUserId(USER_ID, date, pageRequest);
        assertThat(results.getContent())
            .hasSize(2)
            .extracting(MedicationAvailabilityEntity::getId)
            .containsOnly(AVAILABILITY_1_ID, AVAILABILITY_2_ID);
    }

    @Test
    @Sql("classpath:availabilities.sql")
    void findAllNonEmptyNonExpiredByUserId_returnsNothingIfUserNotMatches() {
        LocalDate date = LocalDate.of(2022, 10, 10);
        PageRequest pageRequest = PageRequest.of(0, 10);
        UUID invalidUserId = UUID.fromString("1a808223-9fd3-425e-9953-bbadbf44a79d");
        Page<MedicationAvailabilityEntity> results = repository.findAllNonEmptyNonExpiredByUserId(invalidUserId, date, pageRequest);
        assertThat(results.getContent()).isEmpty();
    }

    @Test
    @Sql("classpath:availabilities.sql")
    void findAllNonEmptyNonExpiredByUserId_returnsPartiallyAvailabilitiesExpiredOnDate() {
        LocalDate date = LocalDate.of(2022, 11, 1);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MedicationAvailabilityEntity> results = repository.findAllNonEmptyNonExpiredByUserId(USER_ID, date, pageRequest);
        assertThat(results.getContent())
            .hasSize(1)
            .extracting(MedicationAvailabilityEntity::getId)
            .containsOnly(AVAILABILITY_2_ID);
    }

    @Test
    @Sql("classpath:availabilities.sql")
    void findByIdAndUserId_returnsEntityIfIdsMatch() {
        Optional<MedicationAvailabilityEntity> result = repository.findByIdAndUserId(AVAILABILITY_1_ID, USER_ID);
        assertThat(result).isNotEmpty();
        assertThat(result)
            .get()
            .extracting(MedicationAvailabilityEntity::getId)
            .isEqualTo(AVAILABILITY_1_ID);
    }

    @Test
    @Sql("classpath:availabilities.sql")
    void findAllNonEmptyNonExpiredByUserIdAndMedicationId_returnsResults() {
        LocalDate date = LocalDate.of(2022, 10, 10);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MedicationAvailabilityEntity> results = repository.findAllNonEmptyNonExpiredByUserIdAndMedicationId(USER_ID, MEDICATION_1_ID, date, pageRequest);
        assertThat(results.getContent())
            .hasSize(1)
            .extracting(MedicationAvailabilityEntity::getId)
            .containsOnly(AVAILABILITY_1_ID);
    }

    @Test
    @Sql("classpath:availabilities.sql")
    void findAllNonEmptyNonExpiredByUserIdAndMedicationId_returnsNothingIfMedicationIdDoesNotMatch() {
        LocalDate date = LocalDate.of(2022, 10, 10);
        PageRequest pageRequest = PageRequest.of(0, 10);
        UUID invalidMedicationId = UUID.fromString("dd8c8540-b080-4832-a663-ff8d2c195ae9");
        Page<MedicationAvailabilityEntity> results = repository.findAllNonEmptyNonExpiredByUserIdAndMedicationId(USER_ID, invalidMedicationId, date, pageRequest);
        assertThat(results.getContent()).isEmpty();
    }

    @Test
    @Sql("classpath:availabilities.sql")
    void findAllNonEmptyNonExpiredByUserIdAndMedicationId_returnsNothingIfUserIdNotMatches() {
        LocalDate date = LocalDate.of(2022, 10, 10);
        PageRequest pageRequest = PageRequest.of(0, 10);
        UUID invalidUserId = UUID.fromString("1a808223-9fd3-425e-9953-bbadbf44a79d");
        Page<MedicationAvailabilityEntity> results = repository.findAllNonEmptyNonExpiredByUserIdAndMedicationId(invalidUserId, MEDICATION_1_ID, date, pageRequest);
        assertThat(results.getContent()).isEmpty();
    }

    @Test
    @Sql("classpath:availabilities.sql")
    void findAllNonEmptyNonExpiredByUserIdAndMedicationId_returnsNothingIfAllExpired() {
        LocalDate date = LocalDate.of(2022, 11, 1);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MedicationAvailabilityEntity> results = repository.findAllNonEmptyNonExpiredByUserIdAndMedicationId(USER_ID, MEDICATION_1_ID, date, pageRequest);
        assertThat(results.getContent()).isEmpty();
    }

    @Test
    @Sql("classpath:availabilities.sql")
    void findAllExpiredOnDate_returnsResults() {
        LocalDate date = LocalDate.of(2022, 10, 31);
        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<MedicationAvailabilityEntity> results = repository.findAllExpiredBeforeDate(date, pageRequest);
        assertThat(results.getContent())
            .hasSize(1)
            .extracting(MedicationAvailabilityEntity::getId)
            .contains(AVAILABILITY_1_ID);
    }

    @Test
    @Sql("classpath:availabilities.sql")
    void findAllExpiredOnDate_returnsNothingIfNoAvailabilityExpiresOnDate() {
        LocalDate date = LocalDate.of(2022, 10, 30);
        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<MedicationAvailabilityEntity> results = repository.findAllExpiredBeforeDate(date, pageRequest);
        assertThat(results.getContent()).isEmpty();
    }

    @Test
    @Sql("classpath:availabilities.sql")
    void findAllMedicationIdsWithQuantityPercentageLessThan_returnsAllResults() {
        BigDecimal percentage = new BigDecimal("0.5");
        LocalDate date = LocalDate.of(2022, 10, 10);
        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<LowMedicationAvailabilityInfo> results = repository.findAllMedicationIdsWithQuantityPercentageLessThan(percentage, date, pageRequest);
        assertThat(results.getContent())
            .hasSize(2)
            .extracting(LowMedicationAvailabilityInfo::getMedicationId)
            .containsOnly(MEDICATION_1_ID, MEDICATION_2_ID);
    }

    @Test
    @Sql("classpath:availabilities.sql")
    void findAllMedicationIdsWithQuantityPercentageLessThan_returnsOnlyResultsMatchingPercentage() {
        BigDecimal percentage = new BigDecimal("0.4");
        LocalDate date = LocalDate.of(2022, 10, 10);
        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<LowMedicationAvailabilityInfo> results = repository.findAllMedicationIdsWithQuantityPercentageLessThan(percentage, date, pageRequest);
        assertThat(results.getContent())
            .hasSize(1)
            .extracting(LowMedicationAvailabilityInfo::getMedicationId)
            .containsOnly(MEDICATION_1_ID);
    }

    @Test
    @Sql("classpath:availabilities.sql")
    void findAllMedicationIdsWithQuantityPercentageLessThan_returnsOnlyResultsMatchingExpiryDate() {
        BigDecimal percentage = new BigDecimal("0.5");
        LocalDate date = LocalDate.of(2022, 11, 1);
        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<LowMedicationAvailabilityInfo> results = repository.findAllMedicationIdsWithQuantityPercentageLessThan(percentage, date, pageRequest);
        assertThat(results.getContent())
            .hasSize(1)
            .extracting(LowMedicationAvailabilityInfo::getMedicationId)
            .containsOnly(MEDICATION_2_ID);
    }
}