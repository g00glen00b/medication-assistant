package be.g00glen00b.apps.medicationassistant.availability;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;
import java.time.Period;

@Configuration
@EnableBatchProcessing
@EnableScheduling
@EnableConfigurationProperties(MedicationAvailabilityNotificationProperties.class)
class MedicationAvailabilityNotificationBatchConfiguration {

    @Bean
    public Job notificationJob(JobBuilderFactory factory, Step soonExpiredStep, Step expiredStep, Step lowQuantityStep) {
        return factory
            .get("notificationJob")
            .incrementer(new RunIdIncrementer())
            .start(soonExpiredStep)
            .next(expiredStep)
            .next(lowQuantityStep)
            .build();
    }

    @Bean
    public Step soonExpiredStep(StepBuilderFactory factory, MedicationAvailabilityExpiryReader soonExpiredReader, MedicationAvailabilityNotificationWriter soonExpiredWriter) {
        return factory
            .get("soonExpiredStep")
            .<MedicationAvailability, MedicationAvailability>chunk(10)
            .reader(soonExpiredReader)
            .writer(soonExpiredWriter)
            .build();
    }

    @Bean
    public Step expiredStep(StepBuilderFactory factory, MedicationAvailabilityExpiryReader expiredReader, MedicationAvailabilityNotificationWriter expiredWriter) {
        return factory
            .get("expiredStep")
            .<MedicationAvailability, MedicationAvailability>chunk(10)
            .reader(expiredReader)
            .writer(expiredWriter)
            .build();
    }

    @Bean
    public Step lowQuantityStep(StepBuilderFactory factory, MedicationAvailabilityLowQuantityReader reader, MedicationAvailabilityLowQuantityNotificationWriter writer) {
        return factory
            .get("lowQuantityStep")
            .<MedicationAvailabilityPercentage, MedicationAvailabilityPercentage>chunk(10)
            .reader(reader)
            .writer(writer)
            .build();
    }

    @Bean
    public MedicationAvailabilityExpiryReader soonExpiredReader(Clock clock, MedicationAvailabilityRepository repository, MedicationAvailabilityNotificationProperties properties) {
        return new MedicationAvailabilityExpiryReader(clock, properties.getSoonExpiredPeriod(), repository);
    }

    @Bean
    public MedicationAvailabilityExpiryReader expiredReader(Clock clock, MedicationAvailabilityRepository repository) {
        return new MedicationAvailabilityExpiryReader(clock, Period.ZERO, repository);
    }

    @Bean
    public MedicationAvailabilityLowQuantityReader lowQuantityReader(MedicationAvailabilityRepository repository, MedicationAvailabilityNotificationProperties properties) {
        return new MedicationAvailabilityLowQuantityReader(repository, properties.getLowQuantityPercentage());
    }

    @Bean
    public MedicationAvailabilityNotificationWriter soonExpiredWriter(MedicationAvailabilityService service, NotificationClient client) {
        return new MedicationAvailabilityNotificationWriter(service, client, NotificationClient::createSoonExpired);
    }

    @Bean
    public MedicationAvailabilityNotificationWriter expiredWriter(MedicationAvailabilityService service, NotificationClient client) {
        return new MedicationAvailabilityNotificationWriter(service, client, NotificationClient::createExpired);
    }

    @Bean
    public MedicationAvailabilityLowQuantityNotificationWriter lowQualityWriter(MedicationClient medicationClient, NotificationClient notificationClient) {
        return new MedicationAvailabilityLowQuantityNotificationWriter(medicationClient, notificationClient);
    }
}
