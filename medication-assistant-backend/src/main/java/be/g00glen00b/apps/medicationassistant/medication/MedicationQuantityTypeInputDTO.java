package be.g00glen00b.apps.medicationassistant.medication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class MedicationQuantityTypeInputDTO {
    String name;

    @JsonCreator
    public MedicationQuantityTypeInputDTO(@JsonProperty("name") String name) {
        this.name = name;
    }
}
