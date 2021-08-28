package be.g00glen00b.apps.medicationassistant.medication;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@Value
@ConstructorBinding
@ConfigurationProperties(prefix = "medicationassistant.medication")
public class MedicationProperties {
    MedicationQuantityProperties quantity;

    @Value
    public static class MedicationQuantityProperties {
        List<String> names;
    }
}