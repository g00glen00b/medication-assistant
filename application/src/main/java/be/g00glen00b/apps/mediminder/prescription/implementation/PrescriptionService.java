package be.g00glen00b.apps.mediminder.prescription.implementation;

import be.g00glen00b.apps.mediminder.availability.MedicationAvailabilityFacade;
import be.g00glen00b.apps.mediminder.medication.MedicationDTO;
import be.g00glen00b.apps.mediminder.prescription.PrescriptionDTO;
import be.g00glen00b.apps.mediminder.schedule.MedicationEventDTO;
import be.g00glen00b.apps.mediminder.schedule.MedicationScheduleFacade;
import be.g00glen00b.apps.mediminder.user.UserFacade;
import be.g00glen00b.apps.mediminder.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class PrescriptionService {
    private final MedicationScheduleFacade scheduleFacade;
    private final MedicationAvailabilityFacade availabilityFacade;
    private final UserFacade userFacade;
    private final Clock clock;

    public List<PrescriptionDTO> calculatePrescriptions(UUID userId, LocalDate plannedDate) {
        Map<MedicationDTO, BigDecimal> requiredQuantitiesPerMedication = calculateRequiredQuantitiesPerMedication(userId, plannedDate);
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
}
