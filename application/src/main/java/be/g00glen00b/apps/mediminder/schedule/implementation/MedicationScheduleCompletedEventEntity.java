package be.g00glen00b.apps.mediminder.schedule.implementation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medication_schedule_completed_event")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationScheduleCompletedEventEntity {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private MedicationScheduleEntity schedule;
    private LocalDateTime eventDate;
    private LocalDateTime completedDate;
    @CreatedDate
    private Instant createdDate;
    @LastModifiedDate
    private Instant lastModifiedDate;

    public static MedicationScheduleCompletedEventEntity of(MedicationScheduleEntity schedule, LocalDate eventDate, LocalDateTime completedDate) {
        return new MedicationScheduleCompletedEventEntity(UUID.randomUUID(), schedule, LocalDateTime.of(eventDate, schedule.getTime()), completedDate, null, null);
    }
}
