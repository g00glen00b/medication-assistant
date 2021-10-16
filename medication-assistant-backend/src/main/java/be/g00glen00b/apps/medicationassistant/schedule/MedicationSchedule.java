package be.g00glen00b.apps.medicationassistant.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
class MedicationSchedule {
    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Type(type = "pg-uuid")
    private UUID id;
    private UUID userId;
    private UUID medicationId;
    private UUID quantityTypeId;
    private BigDecimal quantity;
    private LocalDate startingAt;
    private LocalDate endingAt;
    private Period interval;
    private LocalTime time;
    @OneToMany(mappedBy = "schedule")
    private List<MedicationScheduleEvent> events;

    public MedicationSchedule(UUID userId, UUID medicationId, UUID quantityTypeId, BigDecimal quantity, LocalDate startingAt, Period interval, LocalTime time) {
        this.userId = userId;
        this.medicationId = medicationId;
        this.quantityTypeId = quantityTypeId;
        this.quantity = quantity;
        this.startingAt = startingAt;
        this.interval = interval;
        this.time = time;
    }
}
