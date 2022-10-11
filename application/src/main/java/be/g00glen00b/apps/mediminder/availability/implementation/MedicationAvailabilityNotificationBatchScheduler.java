package be.g00glen00b.apps.mediminder.availability.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class MedicationAvailabilityNotificationBatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job notificationJob;
    private final Clock clock;

    @Scheduled(cron = "@hourly")
    public void schedule() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters parameters = new JobParametersBuilder()
            .addDate("date", Date.from(Instant.now(clock)))
            .toJobParameters();
        jobLauncher.run(notificationJob, parameters);
    }
}
