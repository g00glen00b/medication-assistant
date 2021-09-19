package be.g00glen00b.apps.medicationassistant.medication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Value
public class CreateMedicationRequestDTO {
    @NotNull(message = "{medication.name.notNull}")
    @NotEmpty(message = "{medication.name.notNull}")
    String name;

    @JsonCreator
    public CreateMedicationRequestDTO(@JsonProperty("name") String name) {
        this.name = name;
    }
}
