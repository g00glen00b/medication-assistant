package be.g00glen00b.apps.mediminder.availability.implementation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;
import java.time.Period;

@ConfigurationProperties(prefix = "mediminder.notification.batch")
public record NotificationBatchProperties(
    @DefaultValue("100") int chunkSize,
    @DefaultValue("7d") Period expiryMargin,
    @DefaultValue("20") BigDecimal quantityPercentageMargin) {
}
