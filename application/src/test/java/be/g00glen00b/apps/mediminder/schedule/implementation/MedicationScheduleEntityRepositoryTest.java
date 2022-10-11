package be.g00glen00b.apps.mediminder.schedule.implementation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:tc:postgresql:14.5-alpine3.16:///mediminder"
})
class MedicationScheduleEntityRepositoryTest {
    private static final UUID USER_1_ID = UUID.fromString("0a808223-9fd3-425e-9953-bbadbf44a79d");
    private static final UUID USER_2_ID = UUID.fromString("2b5ecbc9-7665-4263-9b75-16bc3f05e352");
    private static final UUID SCHEDULE_1_ID = UUID.fromString("ea8dea2b-e4a0-47b4-a0c1-b67ed738610f");
    private static final UUID SCHEDULE_2_ID = UUID.fromString("ca14b071-3a82-4739-859e-4b3dea3f9719");
    private static final UUID COMPLETED_EVENT_1_ID = UUID.fromString("abb7322a-824b-4b88-8cdd-4b073ad25483");
    @Autowired
    private MedicationScheduleEntityRepository repository;

    @Test
    @Sql("classpath:schedules.sql")
    void findAllByUserId_returnsResults() {
        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<MedicationScheduleEntity> results = repository.findAllByUserId(USER_1_ID, pageRequest);
        assertThat(results)
            .hasSize(2)
            .extracting(MedicationScheduleEntity::getId)
            .containsOnly(SCHEDULE_1_ID, SCHEDULE_2_ID);
    }

    @Test
    @Sql("classpath:schedules.sql")
    void findByIdAndUserId_returnsResults() {
        Optional<MedicationScheduleEntity> result = repository.findByIdAndUserId(SCHEDULE_1_ID, USER_1_ID);
        assertThat(result)
            .isNotEmpty()
            .get()
            .extracting(MedicationScheduleEntity::getId)
            .isEqualTo(SCHEDULE_1_ID);
    }

    @Test
    @Sql("classpath:schedules.sql")
    void findByIdAndUserId_doesNotIncludeResultsOfOtherUser() {
        Optional<MedicationScheduleEntity> result = repository.findByIdAndUserId(SCHEDULE_1_ID, USER_2_ID);
        assertThat(result).isEmpty();
    }

    @Test
    @Sql("classpath:schedules.sql")
    void findAllByUserIdAndDate_returnsResults() {
        PageRequest pageRequest = PageRequest.of(0, 100);
        LocalDate date = LocalDate.of(2022, 10, 1);
        Page<MedicationScheduleEntity> results = repository.findAllByUserIdAndDate(USER_1_ID, date, pageRequest);
        assertThat(results)
            .hasSize(1)
            .extracting(MedicationScheduleEntity::getId)
            .containsOnly(SCHEDULE_1_ID);
    }

    @Test
    @Sql("classpath:schedules.sql")
    void findCompletedEventByIdAndEventDate_returnsResults() {
        LocalDateTime date = LocalDateTime.of(2022, 10, 1, 8, 0);
        Optional<MedicationScheduleCompletedEventEntity> result = repository.findCompletedEventByIdAndEventDate(SCHEDULE_1_ID, date);
        assertThat(result)
            .isNotEmpty()
            .get()
            .extracting(MedicationScheduleCompletedEventEntity::getId)
            .isEqualTo(COMPLETED_EVENT_1_ID);
    }
}