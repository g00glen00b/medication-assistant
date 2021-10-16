package be.g00glen00b.apps.medicationassistant.schedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface MedicationScheduleEventRepository extends JpaRepository<MedicationScheduleEvent, UUID> {
    Page<MedicationScheduleEvent> findAllByScheduleId(UUID scheduleId, Pageable pageable);
    @Query("select e from MedicationScheduleEvent e inner join e.schedule s where s.userId = ?1 and e.active = true")
    Page<MedicationScheduleEvent> findAllActiveByUserId(UUID userId, Pageable pageable);
    @Modifying
    @Query("delete from MedicationScheduleEvent e where e.schedule.id = ?1 and e.schedule.userId = ?2")
    void deleteAllByScheduleIdAndUserId(UUID scheduleId, UUID userId);
}
