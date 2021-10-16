package be.g00glen00b.apps.medicationassistant.schedule;

import be.g00glen00b.apps.medicationassistant.medication.MedicationDTO;
import be.g00glen00b.apps.medicationassistant.medication.MedicationQuantityTypeDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class MedicationScheduleEventDTO {
    UUID id;
    MedicationDTO medication;
    MedicationQuantityTypeDTO quantityType;
    BigDecimal quantity;
    LocalDateTime time;

    @JsonCreator
    public MedicationScheduleEventDTO(
        @JsonProperty("id") UUID id,
        @JsonProperty("medication") MedicationDTO medication,
        @JsonProperty("quantityType") MedicationQuantityTypeDTO quantityType,
        @JsonProperty("quantity") BigDecimal quantity,
        @JsonProperty("time") LocalDateTime time) {
        this.id = id;
        this.medication = medication;
        this.quantityType = quantityType;
        this.quantity = quantity;
        this.time = time;
    }

    public MedicationScheduleEventDTO(MedicationScheduleEvent event, MedicationDTO medication, MedicationQuantityTypeDTO quantityType) {
        this(event.getId(), medication, quantityType, event.getSchedule().getQuantity(), event.getTime());
    }
}
