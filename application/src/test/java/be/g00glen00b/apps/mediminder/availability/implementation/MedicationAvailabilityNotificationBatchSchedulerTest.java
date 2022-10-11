package be.g00glen00b.apps.mediminder.availability.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MedicationAvailabilityNotificationBatchSchedulerTest {
    private static final ZonedDateTime TODAY = ZonedDateTime.of(2022, 10, 10, 9, 0, 0, 0, ZoneId.of("UTC"));
    private MedicationAvailabilityNotificationBatchScheduler scheduler;
    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private Job notificationJob;
    @Captor
    private ArgumentCaptor<JobParameters> anyJobParameters;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(TODAY.toInstant(), TODAY.getZone());
        scheduler = new MedicationAvailabilityNotificationBatchScheduler(jobLauncher, notificationJob, fixedClock);
    }

    @Test
    void schedule_runsJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        scheduler.schedule();
        verify(jobLauncher).run(eq(notificationJob), any());
    }

    @Test
    void schedule_passesDateAsParameterToAlwaysStartAJobWithDifferentParameters() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        scheduler.schedule();
        verify(jobLauncher).run(eq(notificationJob), anyJobParameters.capture());
        assertThat(anyJobParameters.getValue().getParameters()).containsKey("date");
        assertThat(anyJobParameters.getValue().getDate("date")).isEqualTo(TODAY.toInstant());
    }
}