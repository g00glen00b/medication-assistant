package be.g00glen00b.apps.mediminder.prescription.implementation;

import be.g00glen00b.apps.mediminder.availability.MedicationAvailabilityFacade;
import be.g00glen00b.apps.mediminder.schedule.MedicationScheduleFacade;
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

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ApplicationModuleTest
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
class PrescriptionServiceValidationTest {
    @Autowired
    private PrescriptionService service;
    @MockBean
    private UserFacade userFacade;
    @MockBean
    private MedicationAvailabilityFacade availabilityFacade;
    @MockBean
    private MedicationScheduleFacade scheduleFacade;

    @Test
    void calculatePrescriptions_validatesDateMaximumTwoYearsInFuture() {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now().plusYears(2).plusDays(1);
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.calculatePrescriptions(userId, date))
            .withMessageEndingWith("The chosen date cannot be more than 2 years in the future");
    }
}