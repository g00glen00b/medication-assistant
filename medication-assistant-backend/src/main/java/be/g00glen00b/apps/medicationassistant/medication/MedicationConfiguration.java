package be.g00glen00b.apps.medicationassistant.medication;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MedicationProperties.class)
class MedicationConfiguration {
}
