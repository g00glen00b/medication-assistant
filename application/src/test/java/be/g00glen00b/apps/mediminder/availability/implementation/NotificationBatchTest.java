package be.g00glen00b.apps.mediminder.availability.implementation;

import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationFacade;
import be.g00glen00b.apps.mediminder.medication.MedicationQuantityTypeDTO;
import be.g00glen00b.apps.mediminder.notification.CreateOrUpdateNotificationRequestDTO;
import be.g00glen00b.apps.mediminder.notification.NotificationFacade;
import be.g00glen00b.apps.mediminder.notification.NotificationType;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.*;

@Testcontainers
@SpringBatchTest
@EnableAutoConfiguration
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestEntityManager
@Import({
    NotificationBatchConfiguration.class,
    NotificationBatchTest.DummyConfiguration.class,
    MedicationAvailabilityNotificationService.class,
})
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:tc:postgresql:14.5-alpine3.16:///mediminder",
    "logging.level.root=info"
})
class NotificationBatchTest {
    private static final UUID MEDICATION_HYDROCORTISONE_ID = UUID.fromString("ed8c8540-b080-4832-a663-ff8d2c195ae9");
    private static final UUID MEDICATION_SUSTANON_ID = UUID.fromString("72ae4f14-08fe-43ea-8b65-a8a87ccdc6ef");
    private static final UUID MEDICATION_GENOTONORM_ID = UUID.fromString("2deea386-9ae9-45c8-881d-bb3cc12d8d51");
    private static final UUID QUANTITY_TYPE_ID = UUID.fromString("acac00f8-cf2a-4427-8a1b-e00ab06b1111");
    private static final UUID USER_1_ID = UUID.fromString("0a808223-9fd3-425e-9953-bbadbf44a79d");
    private static final UUID USER_2_ID = UUID.fromString("b40fbbac-60c5-478c-89ad-198e38e2d457");
    private static final UUID USER_3_ID = UUID.fromString("f49ae7d4-ee91-4832-a81c-a4e731a29dfe");
    private static final UUID USER_4_ID = UUID.fromString("31144060-5894-48a8-a40d-18de1100085f");
    private static final UUID AVAILABILITY_1_ID = UUID.fromString("e38e146f-a13c-4c54-a02a-b4547126f1f6");
    private static final UUID AVAILABILITY_3_ID = UUID.fromString("0960ab5f-8a2b-4954-a0fb-22b89f262075");

    private static final UUID AVAILABILITY_4_ID = UUID.fromString("1a921354-a131-429b-8f1f-7b40e59c8b1a");
    private static final UUID AVAILABILITY_5_ID = UUID.fromString("89251a0d-ebef-4a2e-8472-09e9087bfd5a");
    private static final ZonedDateTime TODAY = ZonedDateTime.of(2022, 10, 10, 15, 46, 0, 0, ZoneId.of("UTC"));
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MedicationAvailabilityEntityRepository availabilityEntityRepository;
    @MockBean
    private NotificationFacade notificationFacade;
    @MockBean
    private MedicationFacade medicationFacade;
    @Captor
    private ArgumentCaptor<UUID> anyUUID;
    @Captor
    private ArgumentCaptor<CreateOrUpdateNotificationRequestDTO> anyCreateNotificationRequest;

    @AfterEach
    void tearDown() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "classpath:notification-availabilities.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "classpath:cleanup-notification-availabilities.sql")
    void notificationJob() throws Exception {
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(QUANTITY_TYPE_ID, "#");
        MedicationDTO hydrocortisone = new MedicationDTO(MEDICATION_HYDROCORTISONE_ID, "Hydrocortisone", quantityType);
        MedicationDTO sustanon = new MedicationDTO(MEDICATION_SUSTANON_ID, "Sustanon", quantityType);
        MedicationDTO genotonorm = new MedicationDTO(MEDICATION_GENOTONORM_ID, "Genotonorm", quantityType);
        when(medicationFacade.findByIdOrDummy(MEDICATION_HYDROCORTISONE_ID)).thenReturn(hydrocortisone);
        when(medicationFacade.findByIdOrDummy(MEDICATION_SUSTANON_ID)).thenReturn(sustanon);
        when(medicationFacade.findByIdOrDummy(MEDICATION_GENOTONORM_ID)).thenReturn(genotonorm);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(uniqueParameters());
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        verify(notificationFacade, times(7)).createOrUpdate(anyUUID.capture(), anyCreateNotificationRequest.capture());
        assertThat(captorTuples(anyUUID, anyCreateNotificationRequest))
            .contains(
                tuple(USER_1_ID, new CreateOrUpdateNotificationRequestDTO(NotificationType.WARNING, "AVAILABILITY-ALMOST-NO-QUANTITY-" + MEDICATION_HYDROCORTISONE_ID + "-" + getEpochForAvailability(AVAILABILITY_1_ID), "You're almost running out of Hydrocortisone")),
                tuple(USER_4_ID, new CreateOrUpdateNotificationRequestDTO(NotificationType.WARNING, "AVAILABILITY-ALMOST-NO-QUANTITY-" + MEDICATION_SUSTANON_ID + "-" + getEpochForAvailability(AVAILABILITY_4_ID), "You're almost running out of Sustanon")),
                tuple(USER_2_ID, new CreateOrUpdateNotificationRequestDTO(NotificationType.ERROR, "AVAILABILITY-NO-QUANTITY-" + MEDICATION_GENOTONORM_ID + "-" + getEpochForAvailability(AVAILABILITY_3_ID), "You have no Genotonorm left")),
                tuple(USER_2_ID, new CreateOrUpdateNotificationRequestDTO(NotificationType.WARNING, "AVAILABILITY-ALMOST-NO-QUANTITY-" + MEDICATION_GENOTONORM_ID + "-" + getEpochForAvailability(AVAILABILITY_3_ID), "You're almost running out of Genotonorm")),
                tuple(USER_4_ID, new CreateOrUpdateNotificationRequestDTO(NotificationType.WARNING, "AVAILABILITY-ALMOST-EXPIRED-" + AVAILABILITY_4_ID, "Medication Sustanon with expiry date 2022-10-10 is expiring soon")),
                tuple(USER_4_ID, new CreateOrUpdateNotificationRequestDTO(NotificationType.ERROR, "AVAILABILITY-EXPIRED-" + AVAILABILITY_4_ID, "Medication Sustanon with expiry date 2022-10-10 is expiring today")),
                tuple(USER_3_ID, new CreateOrUpdateNotificationRequestDTO(NotificationType.WARNING, "AVAILABILITY-ALMOST-EXPIRED-" + AVAILABILITY_5_ID, "Medication Hydrocortisone with expiry date 2022-10-11 is expiring soon")));
    }

    private static <A, B> Collection<Tuple> captorTuples(ArgumentCaptor<A> aCaptor, ArgumentCaptor<B> bCaptor) {
        List<A> allAValues = aCaptor.getAllValues();
        List<B> allBValues = bCaptor.getAllValues();
        List<Tuple> allTuples = new ArrayList<>();
        if (allAValues.size() != allBValues.size()) throw new UnsupportedOperationException("Both captured groups must have the same size");
        for (int index = 0; index < allAValues.size(); index++) {
            allTuples.add(tuple(allAValues.get(index), allBValues.get(index)));
        }
        return List.copyOf(allTuples);
    }

    private long getEpochForAvailability(UUID availabilityId) {
        return availabilityEntityRepository
            .findById(availabilityId)
            .map(MedicationAvailabilityEntity::getCreatedDate)
            .map(Instant::toEpochMilli)
            .orElse(0L);
    }

    private static JobParameters uniqueParameters() {
        return new JobParametersBuilder()
            .addDate("date", new Date())
            .toJobParameters();
    }

    @TestConfiguration
    static class DummyConfiguration {
        @Bean
        public Clock clock() {
            return Clock.fixed(TODAY.toInstant(), TODAY.getZone());
        }
    }
}