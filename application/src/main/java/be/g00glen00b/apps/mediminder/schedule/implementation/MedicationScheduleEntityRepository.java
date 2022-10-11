package be.g00glen00b.apps.mediminder.schedule.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

interface MedicationScheduleEntityRepository extends JpaRepository<MedicationScheduleEntity, UUID> {
    Page<MedicationScheduleEntity> findAllByUserId(UUID userId, Pageable pageable);
    Optional<MedicationScheduleEntity> findByIdAndUserId(UUID id, UUID userId);
    @Query("""
        select s from MedicationScheduleEntity s
        where s.userId = ?1 
        and s.period.startingAt <= ?2 
        and (s.period.endingAtInclusive is null or s.period.endingAtInclusive >= ?2)
    """)
    Page<MedicationScheduleEntity> findAllByUserIdAndDate(UUID userId, LocalDate date, Pageable pageable);
    @Query("""
        select e from MedicationScheduleCompletedEventEntity e
        inner join e.schedule s
        where s.id = ?1
        and e.eventDate = ?2
    """)
    Optional<MedicationScheduleCompletedEventEntity> findCompletedEventByIdAndEventDate(UUID id, LocalDateTime eventDate);
}
