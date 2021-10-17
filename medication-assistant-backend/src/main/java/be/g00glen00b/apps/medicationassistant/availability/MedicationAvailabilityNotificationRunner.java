package be.g00glen00b.apps.medicationassistant.availability;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class MedicationAvailabilityNotificationRunner {
    private final JobLauncher jobLauncher;
    private final Job notificationJob;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void launchNotificationsJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(notificationJob, new JobParametersBuilder()
            .addDate("date", new Date())
            .toJobParameters());
    }
}
