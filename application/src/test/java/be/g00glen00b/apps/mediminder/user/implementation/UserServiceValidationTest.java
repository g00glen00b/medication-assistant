package be.g00glen00b.apps.mediminder.user.implementation;

import be.g00glen00b.apps.mediminder.user.CreateUserRequestDTO;
import be.g00glen00b.apps.mediminder.user.UpdateCredentialsRequestDTO;
import be.g00glen00b.apps.mediminder.user.UpdateUserRequestDTO;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZoneId;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ApplicationModuleTest
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@ActiveProfiles("test")
@Import(UserServiceValidationTest.DummyConfiguration.class)
class UserServiceValidationTest {
    @Autowired
    private UserService service;
    @MockBean
    private UserEntityRepository repository;

    @Test
    void create_validatesEmailNotNull() {
        CreateUserRequestDTO request = new CreateUserRequestDTO(null, "Alicia Williams", "password", ZoneId.of("Europe/Brussels"));
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(request))
            .withMessageEndingWith("The e-mail is required");
    }

    @Test
    void create_validatesValidEmail() {
        CreateUserRequestDTO request = new CreateUserRequestDTO("doesnotexist@", "Alicia Williams", "password", ZoneId.of("Europe/Brussels"));
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(request))
            .withMessageEndingWith("The e-mail must be valid");
    }

    @Test
    void create_validatesNameSize() {
        String veryLongName = String.join("", Collections.nCopies(30, "Verylongname"));
        CreateUserRequestDTO request = new CreateUserRequestDTO("me@example.org", veryLongName, "password", ZoneId.of("Europe/Brussels"));
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(request))
            .withMessageEndingWith("The name cannot be longer than 256 characters");
    }

    @Test
    void create_validatesPasswordNotNull() {
        CreateUserRequestDTO request = new CreateUserRequestDTO("me@example.org", "Alicia Williams", null, ZoneId.of("Europe/Brussels"));
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(request))
            .withMessageEndingWith("The password is required");
    }

    @Test
    void create_validatesTimezoneNotNull() {
        CreateUserRequestDTO request = new CreateUserRequestDTO("me@example.org", "Alicia Williams", "password", null);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(request))
            .withMessageEndingWith("The timezone is required");
    }

    @Test
    void update_validatesTimezoneNotNull() {
        UUID id = UUID.randomUUID();
        UpdateUserRequestDTO request = new UpdateUserRequestDTO("Alicia Williams", null);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(id, request))
            .withMessageEndingWith("The timezone is required");
    }

    @Test
    void update_validatesNameSize() {
        UUID id = UUID.randomUUID();
        String veryLongName = String.join("", Collections.nCopies(30, "Verylongname"));
        UpdateUserRequestDTO request = new UpdateUserRequestDTO(veryLongName, ZoneId.of("Europe/Brussels"));
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(id, request))
            .withMessageEndingWith("The name cannot be longer than 256 characters");
    }

    @Test
    void update_validatesOldPasswordNotNull() {
        UUID id = UUID.randomUUID();
        UpdateCredentialsRequestDTO request = new UpdateCredentialsRequestDTO(null, "password");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.updateCredentials(id, request))
            .withMessageEndingWith("The original password is required");
    }
    @Test
    void update_validatesNewPasswordNotNull() {
        UUID id = UUID.randomUUID();
        UpdateCredentialsRequestDTO request = new UpdateCredentialsRequestDTO("password", null);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.updateCredentials(id, request))
            .withMessageEndingWith("The new password is required");
    }


    @TestConfiguration
    @EnableWebSecurity
    static class DummyConfiguration {

    }
}