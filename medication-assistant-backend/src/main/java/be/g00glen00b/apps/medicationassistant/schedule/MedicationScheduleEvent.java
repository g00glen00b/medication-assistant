package be.g00glen00b.apps.medicationassistant.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
class MedicationScheduleEvent {
    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Type(type = "pg-uuid")
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private MedicationSchedule schedule;
    private LocalDateTime time;
    private boolean active;

    public MedicationScheduleEvent(MedicationSchedule schedule, LocalDateTime time) {
        this.schedule = schedule;
        this.time = time;
        this.active = true;
    }

    public MedicationScheduleEvent(MedicationSchedule schedule) {
        this.schedule = schedule;
        this.time = schedule.getStartingAt().atTime(schedule.getTime());
        this.active = true;
    }

    public static Optional<MedicationScheduleEvent> create(MedicationSchedule schedule, LocalDate date) {
        if (schedule.getEndingAt() != null && date.isAfter(schedule.getEndingAt()))  {
            return Optional.empty();
        } else {
            return Optional.of(new MedicationScheduleEvent(schedule, date.atTime(schedule.getTime())));
        }
    }

    public Optional<MedicationScheduleEvent> next() {
        LocalDate date = getTime().plus(schedule.getInterval()).toLocalDate();
        return MedicationScheduleEvent.create(schedule, date);
    }

    public void deactivate() {
        this.active = false;
    }
}
