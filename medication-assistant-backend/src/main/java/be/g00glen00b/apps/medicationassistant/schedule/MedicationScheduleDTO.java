package be.g00glen00b.apps.medicationassistant.schedule;

import be.g00glen00b.apps.medicationassistant.medication.MedicationDTO;
import be.g00glen00b.apps.medicationassistant.medication.MedicationQuantityTypeDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.UUID;

@Value
public class MedicationScheduleDTO {
    UUID id;
    UUID userId;
    MedicationDTO medication;
    MedicationQuantityTypeDTO quantityType;
    BigDecimal quantity;
    LocalDate startingAt;
    LocalDate endingAt;
    Period interval;
    LocalTime time;

    @JsonCreator
    public MedicationScheduleDTO(
        @JsonProperty("id") UUID id,
        @JsonProperty("userId") UUID userId,
        @JsonProperty("medication") MedicationDTO medication,
        @JsonProperty("quantityType") MedicationQuantityTypeDTO quantityType,
        @JsonProperty("quantity") BigDecimal quantity,
        @JsonProperty("startingAt") LocalDate startingAt,
        @JsonProperty("endingAt") LocalDate endingAt,
        @JsonProperty("interval") Period interval,
        @JsonProperty("time") LocalTime time) {
        this.id = id;
        this.userId = userId;
        this.medication = medication;
        this.quantityType = quantityType;
        this.quantity = quantity;
        this.startingAt = startingAt;
        this.endingAt = endingAt;
        this.interval = interval;
        this.time = time;
    }


    public MedicationScheduleDTO(MedicationQuantityTypeDTO quantityType, MedicationDTO medication, MedicationSchedule schedule) {
        this(
            schedule.getId(),
            schedule.getUserId(),
            medication,
            quantityType,
            schedule.getQuantity(),
            schedule.getStartingAt(),
            schedule.getEndingAt(),
            schedule.getInterval(),
            schedule.getTime());
    }
}
