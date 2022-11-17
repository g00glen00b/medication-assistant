package be.g00glen00b.apps.mediminder.prescription;

import be.g00glen00b.apps.mediminder.prescription.implementation.DateWithinPeriod;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PrescriptionFacade {
    List<PrescriptionDTO> calculatePrescriptions(UUID userId, @Valid @DateWithinPeriod(value = "P2Y", message = "{plannedDate.period}") LocalDate plannedDate);
}
