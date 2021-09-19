package be.g00glen00b.apps.medicationassistant.authentication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class CreateUserRequestDTO {
    @NotNull(message = "{user.email.notNull}")
    @Email(message = "{user.email.email}")
    String email;
    String firstName;
    String lastName;
    @NotNull(message = "{user.password.notNull}")
    @Size(min = 5, message = "{user.password.min}")
    String password;

    @JsonCreator
    public CreateUserRequestDTO(
        @JsonProperty("email") String email,
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("password") String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
