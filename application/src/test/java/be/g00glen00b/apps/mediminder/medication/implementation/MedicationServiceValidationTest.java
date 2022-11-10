package be.g00glen00b.apps.mediminder.medication.implementation;

import be.g00glen00b.apps.mediminder.medication.CreateMedicationRequestDTO;
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

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ApplicationModuleTest
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@Import(MedicationServiceValidationTest.DummyConfiguration.class)
class MedicationServiceValidationTest {
    @Autowired
    private MedicationService service;
    @MockBean
    private MedicationEntityRepository repository;
    @MockBean
    private MedicationQuantityTypeEntityRepository quantityTypeEntityRepository;

    @Test
    void create_validatesNameNotNull() {
        UUID quantityTypeId = UUID.randomUUID();
        CreateMedicationRequestDTO request = new CreateMedicationRequestDTO(null, quantityTypeId);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.findOrCreate(request))
            .withMessageEndingWith("The name is required");
    }

    @Test
    void create_validatesNameNotEmpty() {
        String name = "";
        UUID quantityTypeId = UUID.randomUUID();
        CreateMedicationRequestDTO request = new CreateMedicationRequestDTO(name, quantityTypeId);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.findOrCreate(request))
            .withMessageEndingWith("The name is required");
    }

    @Test
    void create_validatesNameSize() {
        String name = String.join("", Collections.nCopies(30, "verylongname"));
        UUID quantityTypeId = UUID.randomUUID();
        CreateMedicationRequestDTO request = new CreateMedicationRequestDTO(name, quantityTypeId);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.findOrCreate(request))
            .withMessageEndingWith("The name cannot be longer than 256 characters");
    }

    @Test
    void create_validatesQuantityTypeNotNull() {
        String name = "Hydrocortisone";
        CreateMedicationRequestDTO request = new CreateMedicationRequestDTO(name, null);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.findOrCreate(request))
            .withMessageEndingWith("The quantity type is required");
    }

    @TestConfiguration
    @EnableWebSecurity
    static class DummyConfiguration {

    }
}