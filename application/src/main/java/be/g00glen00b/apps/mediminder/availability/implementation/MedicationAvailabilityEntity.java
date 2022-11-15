package be.g00glen00b.apps.mediminder.availability.implementation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static be.g00glen00b.apps.mediminder.availability.implementation.BigDecimalUtilities.isPositive;


@Entity
@Table(name = "medication_availability")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationAvailabilityEntity {
    @Id
    private UUID id;
    private UUID medicationId;
    private UUID userId;
    private BigDecimal quantity;
    private BigDecimal initialQuantity;
    private LocalDate expiryDate;
    @CreatedDate
    private Instant createdDate;
    @LastModifiedDate
    private Instant lastModifiedDate;

    public static MedicationAvailabilityEntity of(UUID medicationId, UUID userId, BigDecimal quantity, BigDecimal initialQuantity, LocalDate expiryDate) {
        return new MedicationAvailabilityEntity(UUID.randomUUID(), medicationId, userId, quantity, initialQuantity == null ? quantity : initialQuantity, expiryDate, null, null);
    }

    public boolean isEmpty() {
        return !isPositive(this.quantity);
    }

    public boolean isContaining(BigDecimal quantity) {
        return this.quantity.compareTo(quantity) >= 0;
    }

    public BigDecimal subtractQuantity(BigDecimal quantity) {
        if (!isPositive(quantity)) return BigDecimal.ZERO;
        if (isEmpty()) return quantity;
        if (isContaining(quantity)) {
            this.quantity = this.quantity.subtract(quantity);
            return BigDecimal.ZERO;
        } else {
            BigDecimal remainder = quantity.subtract(this.quantity);
            this.quantity = BigDecimal.ZERO;
            return remainder;
        }

    }
}
