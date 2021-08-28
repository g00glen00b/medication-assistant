package be.g00glen00b.apps.medicationassistant.availability;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MedicationAvailability {
    @EmbeddedId
    private MedicationAvailabilityId id;
    private BigDecimal quantity;
    private UUID quantityTypeId;

    public MedicationAvailability(MedicationAvailabilityId id, UUID quantityTypeId) {
        this.id = id;
        this.quantityTypeId = quantityTypeId;
        this.quantity = BigDecimal.ZERO;
    }
}
