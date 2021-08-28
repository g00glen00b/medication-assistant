package be.g00glen00b.apps.medicationassistant.medication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class MedicationInputDTO {
    String name;

    @JsonCreator
    public MedicationInputDTO(@JsonProperty("name") String name) {
        this.name = name;
    }
}
