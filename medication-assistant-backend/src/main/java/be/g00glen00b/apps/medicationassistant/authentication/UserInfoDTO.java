package be.g00glen00b.apps.medicationassistant.authentication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.UUID;

@Value
public class UserInfoDTO {
    UUID id;
    String email;
    String firstName;
    String lastName;

    @JsonCreator
    public UserInfoDTO(
        @JsonProperty("id") UUID id,
        @JsonProperty("email") String email,
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserInfoDTO(User user) {
        this.id = user.id;
        this.email = user.email;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
    }
}
