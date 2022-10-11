package be.g00glen00b.apps.mediminder.notification.implementation;

import be.g00glen00b.apps.mediminder.notification.CreateOrUpdateNotificationRequestDTO;
import be.g00glen00b.apps.mediminder.notification.NotificationType;
import org.junit.jupiter.api.Test;
import org.moduliths.test.ModuleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ModuleTest
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@Import(NotificationServiceValidationTest.DummyConfiguration.class)
class NotificationServiceValidationTest {
    @Autowired
    private NotificationService service;
    @MockBean
    private NotificationEntityRepository repository;

    @Test
    void create_validatesTypeNotNull() {
        String reference = "REF-123";
        String message = "Message";
        UUID userId = UUID.randomUUID();
        CreateOrUpdateNotificationRequestDTO request = new CreateOrUpdateNotificationRequestDTO(null, reference, message);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.createOrUpdate(userId, request))
            .withMessageEndingWith("The type of notification is required");
    }

    @Test
    void create_validatesMessageNotNull() {
        NotificationType type = NotificationType.INFO;
        String reference = "REF-123";
        UUID userId = UUID.randomUUID();
        CreateOrUpdateNotificationRequestDTO request = new CreateOrUpdateNotificationRequestDTO(type, reference, null);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.createOrUpdate(userId, request))
            .withMessageEndingWith("The message of the notification is required");
    }

    @Test
    void create_validatesMessageNotEmpty() {
        NotificationType type = NotificationType.INFO;
        String reference = "REF-123";
        String message = "";
        UUID userId = UUID.randomUUID();
        CreateOrUpdateNotificationRequestDTO request = new CreateOrUpdateNotificationRequestDTO(type, reference, message);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.createOrUpdate(userId, request))
            .withMessageEndingWith("The message of the notification is required");
    }

    @Test
    void create_validatesMessageNotTooLong() {
        NotificationType type = NotificationType.INFO;
        String reference = "REF-123";
        String message = String.join("", Collections.nCopies(60, "verylongmessage"));
        UUID userId = UUID.randomUUID();
        CreateOrUpdateNotificationRequestDTO request = new CreateOrUpdateNotificationRequestDTO(type, reference, message);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.createOrUpdate(userId, request))
            .withMessageEndingWith("The message of the notification cannot be longer than 512 characters");
    }

    @Test
    void create_validatesReferenceNotNull() {
        NotificationType type = NotificationType.INFO;
        String message = "Message";
        UUID userId = UUID.randomUUID();
        CreateOrUpdateNotificationRequestDTO request = new CreateOrUpdateNotificationRequestDTO(type, null, message);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.createOrUpdate(userId, request))
            .withMessageEndingWith("The reference of the notification is required");
    }

    @Test
    void create_validatesReferenceNotEmpty() {
        NotificationType type = NotificationType.INFO;
        String reference = "";
        String message = "Message";
        UUID userId = UUID.randomUUID();
        CreateOrUpdateNotificationRequestDTO request = new CreateOrUpdateNotificationRequestDTO(type, reference, message);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.createOrUpdate(userId, request))
            .withMessageEndingWith("The reference of the notification is required");
    }

    @Test
    void create_validatesReferenceSize() {
        NotificationType type = NotificationType.INFO;
        String reference = String.join("", Collections.nCopies(20, "verylongreference"));
        String message = "Message";
        UUID userId = UUID.randomUUID();
        CreateOrUpdateNotificationRequestDTO request = new CreateOrUpdateNotificationRequestDTO(type, reference, message);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.createOrUpdate(userId, request))
            .withMessageEndingWith("The reference of the notification cannot be longer than 128 characters");
    }

    @TestConfiguration
    @EnableWebSecurity
    static class DummyConfiguration {

    }
}