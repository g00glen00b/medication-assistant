package be.g00glen00b.apps.medicationassistant.availability;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;
import java.time.Period;

@Value
@ConstructorBinding
@ConfigurationProperties(prefix = "medicationassistant.availability.notification")
public class MedicationAvailabilityNotificationProperties {
    BigDecimal lowQuantityPercentage;
    Period soonExpiredPeriod;

    public MedicationAvailabilityNotificationProperties(
        @DefaultValue("0.05") BigDecimal lowQuantityPercentage,
        @DefaultValue("5D") Period soonExpiredPeriod) {
        this.lowQuantityPercentage = lowQuantityPercentage;
        this.soonExpiredPeriod = soonExpiredPeriod;
    }
}
