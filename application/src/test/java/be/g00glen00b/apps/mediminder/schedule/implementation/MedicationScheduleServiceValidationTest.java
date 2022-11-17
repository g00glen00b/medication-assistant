package be.g00glen00b.apps.mediminder.schedule.implementation;

import be.g00glen00b.apps.mediminder.medication.MedicationFacade;
import be.g00glen00b.apps.mediminder.schedule.CreateMedicationScheduleRequestDTO;
import be.g00glen00b.apps.mediminder.schedule.UpdateMedicationScheduleRequestDTO;
import be.g00glen00b.apps.mediminder.user.UserFacade;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.modulith.test.ApplicationModuleTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ApplicationModuleTest
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
class MedicationScheduleServiceValidationTest {
    @Autowired
    private MedicationScheduleService service;
    @MockBean
    private MedicationScheduleEntityRepository repository;
    @MockBean
    private MedicationFacade medicationFacade;
    @MockBean
    private UserFacade userFacade;

    @Test
    void create_validatesMedicationNotNull() {
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(null, quantity, startingAt, endingAtInclusive, interval, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The medication is required");
    }

    @Test
    void create_validatesQuantityNotNull() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, null, startingAt, endingAtInclusive, interval, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The quantity is required");
    }

    @Test
    void create_validatesQuantityPositive() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("-1");
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, quantity, startingAt, endingAtInclusive, interval, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The quantity has to be a positive number");
    }

    @Test
    void create_validatesStartingAtNotNull() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, quantity, null, endingAtInclusive, interval, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The start date is required");
    }

    @Test
    void create_validatesStartingAtFuture() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.now().minusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, quantity, startingAt, endingAtInclusive, interval, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The start date has to be in the present or the future");
    }

    @Test
    void create_validatesEndingAtInclusiveFuture() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().minusDays(1);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, quantity, startingAt, endingAtInclusive, interval, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The ending date has to be in the future");
    }

    @Test
    void create_validatesIntervalPositive() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        Period interval = Period.ofDays(-1);
        LocalTime time = LocalTime.of(8, 0);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, quantity, startingAt, endingAtInclusive, interval, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The interval has to be positive");
    }

    @Test
    void create_validatesIntervalNotNull() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        LocalTime time = LocalTime.of(8, 0);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, quantity, startingAt, endingAtInclusive, null, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The interval is required");
    }

    @Test
    void create_validatesTimeNotNull() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        Period interval = Period.ofDays(1);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, quantity, startingAt, endingAtInclusive, interval, null, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The time is required");
    }

    @Test
    void create_validatesDescriptionSize() {
        UUID medicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        String description = String.join("", Collections.nCopies(30, "verylongtext"));
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        CreateMedicationScheduleRequestDTO request = new CreateMedicationScheduleRequestDTO(medicationId, quantity, startingAt, endingAtInclusive, interval, time, description);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.create(userId, request))
            .withMessageEndingWith("The description cannot be longer than 256 characters");
    }

    @Test
    void update_validatesQuantityNotNull() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(null, startingAt, endingAtInclusive, interval, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessageEndingWith("The quantity is required");
    }

    @Test
    void update_validatesQuantityPositive() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("-1");
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(quantity, startingAt, endingAtInclusive, interval, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessageEndingWith("The quantity has to be a positive number");
    }

    @Test
    void update_validatesStartingAtNotNull() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(quantity, null, endingAtInclusive, interval, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessageEndingWith("The start date is required");
    }

    @Test
    void update_validatesStartingAtFuture() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.now().minusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(quantity, startingAt, endingAtInclusive, interval, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessageEndingWith("The start date has to be in the present or the future");
    }

    @Test
    void update_validatesEndingAtInclusiveFuture() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().minusDays(1);
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(quantity, startingAt, endingAtInclusive, interval, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessageEndingWith("The ending date has to be in the future");
    }

    @Test
    void update_validatesIntervalPositive() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        Period interval = Period.ofDays(-1);
        LocalTime time = LocalTime.of(8, 0);
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(quantity, startingAt, endingAtInclusive, interval, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessageEndingWith("The interval has to be positive");
    }

    @Test
    void update_validatesIntervalNotNull() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        LocalTime time = LocalTime.of(8, 0);
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(quantity, startingAt, endingAtInclusive, null, time, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessageEndingWith("The interval is required");
    }

    @Test
    void update_validatesTimeNotNull() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        Period interval = Period.ofDays(1);
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(quantity, startingAt, endingAtInclusive, interval, null, "Before breakfast");
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessageEndingWith("The time is required");
    }

    @Test
    void update_validatesDescriptionSize() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("1");
        LocalDate startingAt = LocalDate.now().plusDays(1);
        LocalDate endingAtInclusive = LocalDate.now().plusDays(2);
        String description = String.join("", Collections.nCopies(30, "verylongtext"));
        Period interval = Period.ofDays(1);
        LocalTime time = LocalTime.of(8, 0);
        UpdateMedicationScheduleRequestDTO request = new UpdateMedicationScheduleRequestDTO(quantity, startingAt, endingAtInclusive, interval, time, description);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.update(userId, id, request))
            .withMessageEndingWith("The description cannot be longer than 256 characters");
    }
}