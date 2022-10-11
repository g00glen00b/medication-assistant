package be.g00glen00b.apps.mediminder.availability.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

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
    private final Clock clock;
    private final StepBuilderFactory steps;
    private final JobBuilderFactory jobs;

    @Bean
    public Job notificationJob() {
        return jobs
            .get("epxiryNotificationJob")
            .start(soonExpiredNotificationStep())
            .next(todayExpiredNotificationStep())
            .next(soonNoQuantityNotificationStep())
            .next(noQuantityNotificationStep())
            .build();
    }

    @Bean
    public Step todayExpiredNotificationStep() {
        return steps
            .get("todayExpiredNotificationStep")
            .<MedicationAvailabilityEntity, MedicationAvailabilityEntity>chunk(properties.chunkSize())
            .reader(new MedicationAvailabilityExpiryReader(clock, Period.ZERO, repository, properties.chunkSize()))
            .writer(new MedicationAvailabilityNotificationWriter<>(notificationService::createExpired))
            .build();
    }

    @Bean
    public Step soonExpiredNotificationStep() {
        return steps
            .get("soonExpiredNotificationStep")
            .<MedicationAvailabilityEntity, MedicationAvailabilityEntity>chunk(properties.chunkSize())
            .reader(new MedicationAvailabilityExpiryReader(clock, properties.expiryMargin(), repository, properties.chunkSize()))
            .writer(new MedicationAvailabilityNotificationWriter<>(notificationService::createAlmostExpired))
            .build();
    }

    @Bean
    public Step soonNoQuantityNotificationStep() {
        return steps
            .get("soonNoQuantityNotificationStep")
            .<LowMedicationAvailabilityInfo, LowMedicationAvailabilityInfo>chunk(properties.chunkSize())
            .reader(new MedicationAvailabilityLowQuantityReader(clock, properties.quantityPercentageMargin(), repository, properties.chunkSize()))
            .writer(new MedicationAvailabilityNotificationWriter<>(notificationService::createAlmostNoQuantity))
            .build();
    }

    @Bean
    public Step noQuantityNotificationStep() {
        return steps
            .get("noQuantityNotificationStep")
            .<LowMedicationAvailabilityInfo, LowMedicationAvailabilityInfo>chunk(properties.chunkSize())
            .reader(new MedicationAvailabilityLowQuantityReader(clock, BigDecimal.ZERO, repository, properties.chunkSize()))
            .writer(new MedicationAvailabilityNotificationWriter<>(notificationService::createNoQuantity))
            .build();
    }

}
