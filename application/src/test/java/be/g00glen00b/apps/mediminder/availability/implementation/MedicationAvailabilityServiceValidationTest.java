package be.g00glen00b.apps.mediminder.availability.implementation;

import be.g00glen00b.apps.mediminder.availability.CreateMedicationAvailabilityRequestDTO;
import be.g00glen00b.apps.mediminder.availability.UpdateMedicationAvailabilityRequestDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationFacade;
import be.g00glen00b.apps.mediminder.notification.NotificationFacade;
import be.g00glen00b.apps.mediminder.user.UserFacade;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ApplicationModuleTest
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@Import(MedicationAvailabilityServiceValidationTest.DummyConfiguration.class)
class MedicationAvailabilityServiceValidationTest {
    @Autowired
    private MedicationAvailabilityService service;
    @MockBean
    private MedicationAvailabilityEntityRepository repository;
    @MockBean
    private MedicationFacade medicationFacade;
    @MockBean
    private UserFacade userFacade;
    @MockBean
    private NotificationFacade notificationFacade;

    @Test
    void create_validatesMedicationNameNotNull() {
        UUID userId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        BigDecimal initialQuantity = new BigDecimal("10");
        LocalDate expiryDate = LocalDate.now().plusDays(10);
        CreateMedicationAvailabilityRequestDTO request = new CreateMedicationAvailabilityRequestDTO(null, quantityTypeId, quantity, initialQuantity, expiryDate);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The medication is required");
    }

    @Test
    void create_validatesMedicationNameSize() {
        UUID userId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        BigDecimal initialQuantity = new BigDecimal("10");
        LocalDate expiryDate = LocalDate.now().plusDays(10);
        String veryLongName = String.join("", Collections.nCopies(30, "Verylongname"));
        CreateMedicationAvailabilityRequestDTO request = new CreateMedicationAvailabilityRequestDTO(veryLongName, quantityTypeId, quantity, initialQuantity, expiryDate);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The name of the medication cannot be longer than 256 characters");
    }

    @Test
    void create_validatesQuantityTypeNotNull() {
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        BigDecimal initialQuantity = new BigDecimal("10");
        LocalDate expiryDate = LocalDate.now().plusDays(10);
        String name = "Hydrocortisone";
        CreateMedicationAvailabilityRequestDTO request = new CreateMedicationAvailabilityRequestDTO(name, null, quantity, initialQuantity, expiryDate);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The quantity type is required");
    }

    @Test
    void create_validatesQuantityNotNull() {
        UUID userId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        BigDecimal initialQuantity = new BigDecimal("10");
        LocalDate expiryDate = LocalDate.now().plusDays(10);
        String name = "Hydrocortisone";
        CreateMedicationAvailabilityRequestDTO request = new CreateMedicationAvailabilityRequestDTO(name, quantityTypeId, null, initialQuantity, expiryDate);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The quantity is required");
    }

    @Test
    void create_validatesQuantityPositive() {
        UUID userId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("-1");
        BigDecimal initialQuantity = new BigDecimal("10");
        LocalDate expiryDate = LocalDate.now().plusDays(10);
        String name = "Hydrocortisone";
        CreateMedicationAvailabilityRequestDTO request = new CreateMedicationAvailabilityRequestDTO(name, quantityTypeId, quantity, initialQuantity, expiryDate);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The quantity has to be zero or more");
    }

    @Test
    void create_validatesInitialQuantityPositive() {
        UUID userId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        BigDecimal initialQuantity = new BigDecimal("-10");
        LocalDate expiryDate = LocalDate.now().plusDays(10);
        String name = "Hydrocortisone";
        CreateMedicationAvailabilityRequestDTO request = new CreateMedicationAvailabilityRequestDTO(name, quantityTypeId, quantity, initialQuantity, expiryDate);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The initial quantity has to be zero or more");
    }

    @Test
    void create_validatesExpiryDateFuture() {
        UUID userId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        BigDecimal initialQuantity = new BigDecimal("10");
        LocalDate expiryDate = LocalDate.now().minusDays(1);
        String name = "Hydrocortisone";
        CreateMedicationAvailabilityRequestDTO request = new CreateMedicationAvailabilityRequestDTO(name, quantityTypeId, quantity, initialQuantity, expiryDate);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The expiry date must be in the future");
    }

    @Test
    void update_validatesQuantityNotNull() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        BigDecimal initialQuantity = new BigDecimal("10");
        LocalDate expiryDate = LocalDate.now().plusDays(10);
        UpdateMedicationAvailabilityRequestDTO request = new UpdateMedicationAvailabilityRequestDTO(null, initialQuantity, expiryDate);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessageEndingWith("The quantity is required");
    }

    @Test
    void update_validatesQuantityPositive() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("-1");
        BigDecimal initialQuantity = new BigDecimal("10");
        LocalDate expiryDate = LocalDate.now().plusDays(10);
        UpdateMedicationAvailabilityRequestDTO request = new UpdateMedicationAvailabilityRequestDTO(quantity, initialQuantity, expiryDate);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessageEndingWith("The quantity has to be zero or more");
    }

    @Test
    void update_validatesInitialQuantityPositive() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        BigDecimal initialQuantity = new BigDecimal("-10");
        LocalDate expiryDate = LocalDate.now().plusDays(10);
        UpdateMedicationAvailabilityRequestDTO request = new UpdateMedicationAvailabilityRequestDTO(quantity, initialQuantity, expiryDate);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessageEndingWith("The initial quantity has to be zero or more");
    }

    @Test
    void update_validatesExpiryDateFuture() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        BigDecimal initialQuantity = new BigDecimal("10");
        LocalDate expiryDate = LocalDate.now().minusDays(1);
        UpdateMedicationAvailabilityRequestDTO request = new UpdateMedicationAvailabilityRequestDTO(quantity, initialQuantity, expiryDate);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessageEndingWith("The expiry date must be in the future");
    }

    @TestConfiguration
    @EnableWebSecurity
    static class DummyConfiguration {

    }
}