package be.g00glen00b.apps.mediminder.schedule.implementation;

import be.g00glen00b.apps.mediminder.schedule.MedicationTakenEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "medication_schedule")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationScheduleEntity extends AbstractAggregateRoot<MedicationScheduleEntity> {
    @Id
    private UUID id;
    private UUID medicationId;
    private UUID userId;
    private BigDecimal quantity;
    @Embedded
    private MedicationSchedulePeriod period;
    @Convert(converter = PeriodConverter.class)
    private Period interval;
    private LocalTime time;
    private String description;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<MedicationScheduleCompletedEventEntity> completedEvents;
    @CreatedDate
    private Instant createdDate;
    @LastModifiedDate
    private Instant lastModifiedDate;

    public boolean isActiveOnDate(LocalDate date) {
        if (period.getEndingAtInclusive() != null && date.isAfter(period.getEndingAtInclusive())) return false;
        RecurringPeriod recurringPeriod = new RecurringPeriod(period.getStartingAt(), interval);
        return recurringPeriod.isActiveOn(date);
    }

    public MedicationScheduleCompletedEventEntity addCompletedEvent(LocalDate eventDate, LocalDateTime completedDate) {
        MedicationScheduleCompletedEventEntity entity = MedicationScheduleCompletedEventEntity.of(this, eventDate, completedDate);
        registerEvent(MedicationTakenEvent.ofEntity(this, eventDate, completedDate));
        this.completedEvents.add(entity);
        return entity;
    }

    public static MedicationScheduleEntity of(UUID medicationId, UUID userId, BigDecimal quantity, MedicationSchedulePeriod period, Period interval, LocalTime time, String description) {
        return new MedicationScheduleEntity(UUID.randomUUID(), medicationId, userId, quantity, period, interval.normalized(), time, description, new ArrayList<>(), null, null);
    }
}
