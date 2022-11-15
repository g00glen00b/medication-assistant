package be.g00glen00b.apps.mediminder.availability.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Period;

@Configuration
@EnableBatchProcessing
@EnableScheduling
@EnableConfigurationProperties(NotificationBatchProperties.class)
@RequiredArgsConstructor
public class NotificationBatchConfiguration {
    private final NotificationBatchProperties properties;
    private final MedicationAvailabilityEntityRepository repository;
    private final MedicationAvailabilityNotificationService notificationService;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final Clock clock;

    @Bean
    public Job notificationJob() {
        return new JobBuilder("epxiryNotificationJob", jobRepository)
            .start(soonExpiredNotificationStep())
            .next(todayExpiredNotificationStep())
            .next(soonNoQuantityNotificationStep())
            .next(noQuantityNotificationStep())
            .build();
    }

    @Bean
    public Step todayExpiredNotificationStep() {
        return new StepBuilder("todayExpiredNotificationStep", jobRepository)
            .<MedicationAvailabilityEntity, MedicationAvailabilityEntity>chunk(properties.chunkSize(), transactionManager)
            .reader(new MedicationAvailabilityExpiryReader(clock, Period.ZERO, repository, properties.chunkSize()))
            .writer(new MedicationAvailabilityNotificationWriter<>(notificationService::createExpired))
            .build();
    }

    @Bean
    public Step soonExpiredNotificationStep() {
        return new StepBuilder("soonExpiredNotificationStep", jobRepository)
            .<MedicationAvailabilityEntity, MedicationAvailabilityEntity>chunk(properties.chunkSize(), transactionManager)
            .reader(new MedicationAvailabilityExpiryReader(clock, properties.expiryMargin(), repository, properties.chunkSize()))
            .writer(new MedicationAvailabilityNotificationWriter<>(notificationService::createAlmostExpired))
            .build();
    }

    @Bean
    public Step soonNoQuantityNotificationStep() {
        return new StepBuilder("soonNoQuantityNotificationStep", jobRepository)
            .<LowMedicationAvailabilityInfo, LowMedicationAvailabilityInfo>chunk(properties.chunkSize(), transactionManager)
            .reader(new MedicationAvailabilityLowQuantityReader(clock, properties.quantityPercentageMargin(), repository, properties.chunkSize()))
            .writer(new MedicationAvailabilityNotificationWriter<>(notificationService::createAlmostNoQuantity))
            .build();
    }

    @Bean
    public Step noQuantityNotificationStep() {
        return new StepBuilder("noQuantityNotificationStep", jobRepository)
            .<LowMedicationAvailabilityInfo, LowMedicationAvailabilityInfo>chunk(properties.chunkSize(), transactionManager)
            .reader(new MedicationAvailabilityLowQuantityReader(clock, BigDecimal.ZERO, repository, properties.chunkSize()))
            .writer(new MedicationAvailabilityNotificationWriter<>(notificationService::createNoQuantity))
            .build();
    }

}
