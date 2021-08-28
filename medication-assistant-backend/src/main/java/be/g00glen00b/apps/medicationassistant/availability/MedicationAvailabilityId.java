package be.g00glen00b.apps.medicationassistant.availability;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
// Disabled ORM inspection for embedded ID until IDEA-223439 is fixed
// https://youtrack.jetbrains.com/issue/IDEA-223439
@SuppressWarnings("JpaDataSourceORMInspection")
public class MedicationAvailabilityId implements Serializable {
    @Column(name = "medication_id")
    @Type(type = "pg-uuid")
    private UUID medicationId;
    @Column(name = "user_id")
    @Type(type = "pg-uuid")
    private UUID userId;
}
