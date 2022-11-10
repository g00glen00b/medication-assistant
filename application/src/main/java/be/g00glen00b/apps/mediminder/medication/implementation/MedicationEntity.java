package be.g00glen00b.apps.mediminder.medication.implementation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "medication")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationEntity {
    @Id
    private UUID id;
    private String name;
    @ManyToOne
    private MedicationQuantityTypeEntity quantityType;
    @CreatedDate
    private Instant createdDate;
    @LastModifiedDate
    private Instant lastModifiedDate;

    public static MedicationEntity of(String name, MedicationQuantityTypeEntity quantityType) {
        return new MedicationEntity(UUID.randomUUID(), name, quantityType, null, null);
    }
}
