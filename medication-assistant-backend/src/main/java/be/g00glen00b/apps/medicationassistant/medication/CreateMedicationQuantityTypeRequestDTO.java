package be.g00glen00b.apps.medicationassistant.medication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Value
public class CreateMedicationQuantityTypeRequestDTO {
    @NotNull(message = "{quantity.name.notNull}")
    @NotEmpty(message = "{quantity.name.notNull}")
    String name;

    @JsonCreator
    public CreateMedicationQuantityTypeRequestDTO(@JsonProperty("name") String name) {
        this.name = name;
    }
}
