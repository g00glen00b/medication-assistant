package be.g00glen00b.apps.mediminder.prescription.implementation;

import be.g00glen00b.apps.mediminder.availability.MedicationAvailabilityFacade;
import be.g00glen00b.apps.mediminder.availability.MedicationAvailabilityTotalDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationQuantityTypeDTO;
import be.g00glen00b.apps.mediminder.prescription.PrescriptionDTO;
import be.g00glen00b.apps.mediminder.schedule.MedicationEventDTO;
import be.g00glen00b.apps.mediminder.schedule.MedicationScheduleFacade;
import be.g00glen00b.apps.mediminder.user.UserFacade;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceTest {
    private PrescriptionService service;
    @Mock
    private MedicationAvailabilityFacade availabilityFacade;
    @Mock
    private MedicationScheduleFacade scheduleFacade;
    @Mock
    private UserFacade userFacade;

    @BeforeEach
    void setUp() {
        service = new PrescriptionService(scheduleFacade, availabilityFacade, userFacade);
    }

    @Test
    void calculatePrescriptions_returnsPrescriptions() {
        UUID userId = UUID.randomUUID();
        MedicationDTO medication = aMedication();
        LocalDateTime today = LocalDateTime.of(2022, 11, 10, 8, 0, 0);
        MedicationEventDTO event1 = anUncompletedEvent(medication, new BigDecimal(1), today);
        MedicationEventDTO event2 = anUncompletedEvent(medication, new BigDecimal(2), today);
        MedicationEventDTO event3 = aCompletedEvent(medication, new BigDecimal(4), today);
        MedicationAvailabilityTotalDTO total = new MedicationAvailabilityTotalDTO(medication, new BigDecimal(10), new BigDecimal(1));
        when(userFacade.calculateTodayForUser(any())).thenReturn(today);
        when(scheduleFacade.findEventsByDate(any(), any())).thenReturn(List.of(event1, event2, event3));
        when(availabilityFacade.findAllTotalsByUserId(any())).thenReturn(List.of(total));

        List<PrescriptionDTO> results = service.calculatePrescriptions(userId, today.plusDays(1).toLocalDate());
        assertThat(results)
            .hasSize(1)
            .containsOnly(new PrescriptionDTO(
                medication,
                new BigDecimal(3),
                new BigDecimal(1),
                new BigDecimal(2),
                new BigDecimal(10),
                new BigDecimal(1)
            ));
    }

    @Test
    void calculatePrescriptions_doesNotReturnPrescriptionIfNoNeededQuantity() {
        UUID userId = UUID.randomUUID();
        MedicationDTO medication = aMedication();
        LocalDateTime today = LocalDateTime.of(2022, 11, 10, 8, 0, 0);
        MedicationEventDTO event1 = anUncompletedEvent(medication, new BigDecimal(1), today);
        MedicationEventDTO event2 = anUncompletedEvent(medication, new BigDecimal(2), today);
        MedicationEventDTO event3 = aCompletedEvent(medication, new BigDecimal(4), today);
        MedicationAvailabilityTotalDTO total = new MedicationAvailabilityTotalDTO(medication, new BigDecimal(10), new BigDecimal(3));
        when(userFacade.calculateTodayForUser(any())).thenReturn(today);
        when(scheduleFacade.findEventsByDate(any(), any())).thenReturn(List.of(event1, event2, event3));
        when(availabilityFacade.findAllTotalsByUserId(any())).thenReturn(List.of(total));

        List<PrescriptionDTO> results = service.calculatePrescriptions(userId, today.plusDays(1).toLocalDate());
        assertThat(results).isEmpty();
    }

    @Test
    void calculatePrescriptions_returnsPrescriptionsMultipleDates() {
        UUID userId = UUID.randomUUID();
        MedicationDTO medication = aMedication();
        LocalDateTime today = LocalDateTime.of(2022, 11, 10, 8, 0, 0);
        LocalDate tomorrow = today.plusDays(1).toLocalDate();
        MedicationEventDTO event1 = anUncompletedEvent(medication, new BigDecimal(1), today);
        MedicationEventDTO event2 = anUncompletedEvent(medication, new BigDecimal(2), today);
        MedicationEventDTO event3 = aCompletedEvent(medication, new BigDecimal(4), today);
        MedicationAvailabilityTotalDTO total = new MedicationAvailabilityTotalDTO(medication, new BigDecimal(10), new BigDecimal(3));
        when(userFacade.calculateTodayForUser(any())).thenReturn(today);
        when(scheduleFacade.findEventsByDate(any(), eq(today.toLocalDate()))).thenReturn(List.of(event1));
        when(scheduleFacade.findEventsByDate(any(), eq(tomorrow))).thenReturn(List.of(event2, event3));
        when(availabilityFacade.findAllTotalsByUserId(any())).thenReturn(List.of(total));

        List<PrescriptionDTO> results = service.calculatePrescriptions(userId, today.plusDays(2).toLocalDate());
        assertThat(results).isEmpty();
    }

    @Test
    void calculatePrescriptions_returnsPrescriptionsMultipleMedications() {
        UUID userId = UUID.randomUUID();
        MedicationDTO medication1 = aMedication();
        MedicationDTO medication2 = aMedication();
        LocalDateTime today = LocalDateTime.of(2022, 11, 10, 8, 0, 0);
        LocalDate tomorrow = today.plusDays(1).toLocalDate();
        MedicationEventDTO event1 = anUncompletedEvent(medication1, new BigDecimal(2), today);
        MedicationEventDTO event2 = anUncompletedEvent(medication2, new BigDecimal(4), today);
        MedicationEventDTO event3 = aCompletedEvent(medication1, new BigDecimal(8), today);
        MedicationAvailabilityTotalDTO total1 = new MedicationAvailabilityTotalDTO(medication1, new BigDecimal(10), new BigDecimal(1));
        MedicationAvailabilityTotalDTO total2 = new MedicationAvailabilityTotalDTO(medication2, new BigDecimal(1), new BigDecimal(2));
        when(userFacade.calculateTodayForUser(any())).thenReturn(today);
        when(scheduleFacade.findEventsByDate(any(), eq(today.toLocalDate()))).thenReturn(List.of(event1));
        when(scheduleFacade.findEventsByDate(any(), eq(tomorrow))).thenReturn(List.of(event2, event3));
        when(availabilityFacade.findAllTotalsByUserId(any())).thenReturn(List.of(total1, total2));

        List<PrescriptionDTO> results = service.calculatePrescriptions(userId, today.plusDays(2).toLocalDate());
        assertThat(results)
            .hasSize(2)
            .contains(new PrescriptionDTO(
                medication1,
                new BigDecimal(2),
                new BigDecimal(1),
                new BigDecimal(1),
                new BigDecimal(10),
                new BigDecimal(1)
            ))
            .contains(new PrescriptionDTO(
                medication2,
                new BigDecimal(4),
                new BigDecimal(2),
                new BigDecimal(2),
                new BigDecimal(1),
                new BigDecimal(2)
            ));
    }

    @Test
    void calculatePrescriptions_retrievesUsersToday() {
        UUID userId = UUID.randomUUID();
        MedicationDTO medication = aMedication();
        LocalDateTime today = LocalDateTime.of(2022, 11, 10, 8, 0, 0);
        MedicationEventDTO event1 = anUncompletedEvent(medication, new BigDecimal(1), today);
        MedicationAvailabilityTotalDTO total = new MedicationAvailabilityTotalDTO(medication, new BigDecimal(10), new BigDecimal(1));
        when(userFacade.calculateTodayForUser(any())).thenReturn(today);
        when(scheduleFacade.findEventsByDate(any(), any())).thenReturn(List.of(event1));
        when(availabilityFacade.findAllTotalsByUserId(any())).thenReturn(List.of(total));

        service.calculatePrescriptions(userId, today.plusDays(1).toLocalDate());
        verify(userFacade).calculateTodayForUser(userId);
    }

    @Test
    void calculatePrescriptions_retrievesScheduledMedicationForDate() {
        UUID userId = UUID.randomUUID();
        MedicationDTO medication = aMedication();
        LocalDateTime today = LocalDateTime.of(2022, 11, 10, 8, 0, 0);
        MedicationEventDTO event1 = anUncompletedEvent(medication, new BigDecimal(1), today);
        MedicationAvailabilityTotalDTO total = new MedicationAvailabilityTotalDTO(medication, new BigDecimal(10), new BigDecimal(1));
        when(userFacade.calculateTodayForUser(any())).thenReturn(today);
        when(scheduleFacade.findEventsByDate(any(), any())).thenReturn(List.of(event1));
        when(availabilityFacade.findAllTotalsByUserId(any())).thenReturn(List.of(total));

        service.calculatePrescriptions(userId, today.plusDays(1).toLocalDate());
        verify(scheduleFacade).findEventsByDate(userId, today.toLocalDate());
    }

    @Test
    void calculatePrescriptions_retrievesInventoryTotals() {
        UUID userId = UUID.randomUUID();
        MedicationDTO medication = aMedication();
        LocalDateTime today = LocalDateTime.of(2022, 11, 10, 8, 0, 0);
        MedicationEventDTO event1 = anUncompletedEvent(medication, new BigDecimal(1), today);
        MedicationAvailabilityTotalDTO total = new MedicationAvailabilityTotalDTO(medication, new BigDecimal(10), new BigDecimal(1));
        when(userFacade.calculateTodayForUser(any())).thenReturn(today);
        when(scheduleFacade.findEventsByDate(any(), any())).thenReturn(List.of(event1));
        when(availabilityFacade.findAllTotalsByUserId(any())).thenReturn(List.of(total));

        service.calculatePrescriptions(userId, today.plusDays(1).toLocalDate());
        verify(availabilityFacade).findAllTotalsByUserId(userId);
    }

    private static MedicationEventDTO anUncompletedEvent(MedicationDTO medication, BigDecimal dose, LocalDateTime eventDate) {
        UUID scheduleId = UUID.randomUUID();
        return new MedicationEventDTO(scheduleId, medication, dose, eventDate, null, null);
    }

    private static MedicationEventDTO aCompletedEvent(MedicationDTO medication, BigDecimal dose, LocalDateTime eventDate) {
        UUID scheduleId = UUID.randomUUID();
        return new MedicationEventDTO(scheduleId, medication, dose, eventDate, null, eventDate);
    }

    @NotNull
    private static MedicationDTO aMedication() {
        UUID medicationId = UUID.randomUUID();
        UUID quantityTypeId = UUID.randomUUID();
        MedicationQuantityTypeDTO quantityType = new MedicationQuantityTypeDTO(quantityTypeId, "capsules");
        return new MedicationDTO(medicationId, "Hydrocortisone", quantityType);
    }
}