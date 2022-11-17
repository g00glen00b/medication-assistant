package be.g00glen00b.apps.mediminder.prescription.implementation;

import be.g00glen00b.apps.mediminder.availability.MedicationAvailabilityFacade;
import be.g00glen00b.apps.mediminder.availability.MedicationAvailabilityTotalDTO;
import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.prescription.PrescriptionDTO;
import be.g00glen00b.apps.mediminder.prescription.PrescriptionFacade;
import be.g00glen00b.apps.mediminder.schedule.MedicationEventDTO;
import be.g00glen00b.apps.mediminder.schedule.MedicationScheduleFacade;
import be.g00glen00b.apps.mediminder.user.UserFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.function.Function.identity;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.*;

@Service
@Validated
@RequiredArgsConstructor
public class PrescriptionService implements PrescriptionFacade {
    private final MedicationScheduleFacade scheduleFacade;
    private final MedicationAvailabilityFacade availabilityFacade;
    private final UserFacade userFacade;

    @Override
    public List<PrescriptionDTO> calculatePrescriptions(UUID userId, @Valid @DateWithinPeriod(value = "P2Y", message = "{plannedDate.period}") LocalDate plannedDate) {
        Map<MedicationDTO, BigDecimal> requiredQuantities = calculateRequiredQuantitiesPerMedication(userId, plannedDate);
        Map<MedicationDTO, MedicationAvailabilityTotalDTO> availableQuantities = calculateAvailableQuantitiesPerMedication(userId);
        return new PrescriptionCalculator(requiredQuantities, availableQuantities).calculate();
    }

    private Map<MedicationDTO, BigDecimal> calculateRequiredQuantitiesPerMedication(UUID userId, LocalDate plannedDate) {
        LocalDate today = userFacade.calculateTodayForUser(userId).toLocalDate();
        return today
            .datesUntil(plannedDate)
            .map(date -> scheduleFacade.findEventsByDate(userId, date))
            .flatMap(Collection::stream)
            .filter(not(MedicationEventDTO::isCompleted))
            .collect(
                groupingBy(MedicationEventDTO::medication,
                    mapping(MedicationEventDTO::quantity,
                        reducing(BigDecimal.ZERO, BigDecimal::add))));
    }

    private Map<MedicationDTO, MedicationAvailabilityTotalDTO> calculateAvailableQuantitiesPerMedication(UUID userId) {
        return availabilityFacade
            .findAllTotalsByUserId(userId)
            .stream()
            .collect(toMap(MedicationAvailabilityTotalDTO::medication, identity()));
    }
}
