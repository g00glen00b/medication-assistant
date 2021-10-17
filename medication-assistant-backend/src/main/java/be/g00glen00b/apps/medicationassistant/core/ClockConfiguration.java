package be.g00glen00b.apps.medicationassistant.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class ClockConfiguration {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
