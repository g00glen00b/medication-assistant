package be.g00glen00b.apps.medicationassistant.schedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface MedicationScheduleEventFacade {
    MedicationScheduleEventDTO createNextEvent(UUID scheduleId, UUID userId);
    Page<MedicationScheduleEventDTO> findAllActiveByUserId(UUID userId, Pageable pageable);
    void deleteBySchedule(UUID scheduleId, UUID userId);
}
