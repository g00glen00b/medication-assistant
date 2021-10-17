package be.g00glen00b.apps.medicationassistant.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.UUID;

@Value
public class MedicationScheduleCompletedEventDTO {
    UUID userId;
    MedicationScheduleEventDTO event;

    @JsonCreator
    public MedicationScheduleCompletedEventDTO(
        @JsonProperty("userId") UUID userId,
        @JsonProperty("event") MedicationScheduleEventDTO event) {
        this.userId = userId;
        this.event = event;
    }
}
