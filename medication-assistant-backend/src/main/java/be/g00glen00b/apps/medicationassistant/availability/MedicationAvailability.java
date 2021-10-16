package be.g00glen00b.apps.medicationassistant.availability;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
class MedicationAvailability {
    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Type(type = "pg-uuid")
    private UUID id;
    private UUID userId;
    private UUID medicationId;
    private UUID quantityTypeId;
    private BigDecimal quantity;
    private BigDecimal initialQuantity;
    private LocalDate expiryDate;

    public MedicationAvailability(UUID userId, UUID medicationId, UUID quantityTypeId, BigDecimal quantity, BigDecimal initialQuantity, LocalDate expiryDate) {
        this.userId = userId;
        this.medicationId = medicationId;
        this.quantityTypeId = quantityTypeId;
        this.quantity = quantity;
        this.initialQuantity = initialQuantity;
        this.expiryDate = expiryDate;
    }

    public void setQuantity(BigDecimal newQuantity) {
        if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMedicationAvailabilityException("The quantity cannot be less than zero");
        } else if (newQuantity.compareTo(this.initialQuantity) > 0) {
            throw new InvalidMedicationAvailabilityException("The quantity cannot be more than the initial quantity");
        } else {
           this.quantity = newQuantity;
        }
    }

    public void increaseQuantityByOne() {
        setQuantity(this.quantity.add(BigDecimal.ONE));
    }

    public void decreaseQuantityByOne() {
        setQuantity(this.quantity.subtract(BigDecimal.ONE));
    }
}
