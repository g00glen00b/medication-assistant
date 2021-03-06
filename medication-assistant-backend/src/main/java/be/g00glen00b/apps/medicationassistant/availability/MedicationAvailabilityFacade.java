package be.g00glen00b.apps.medicationassistant.availability;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;

public interface MedicationAvailabilityFacade {
    Page<MedicationAvailabilityDTO> findAllByUserId(UUID userId, Pageable pageable);
    MedicationAvailabilityDTO create(UUID userId, @Valid CreateMedicationAvailabilityRequestDTO input);
    MedicationAvailabilityDTO update(UUID userId, UUID id, @Valid UpdateMedicationAvailabilityInputDTO input);
    MedicationAvailabilityDTO increaseQuantity(UUID userId, UUID id);
    MedicationAvailabilityDTO decreaseQuantity(UUID userId, UUID id);
    void decreaseOverallQuantity(UUID userId, UUID medicationId, UUID quantityTypeId, BigDecimal quantity);
    void delete(UUID userId, UUID id);
}
