package be.g00glen00b.apps.mediminder.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.ZoneId;

public record UpdateUserRequestDTO(
    @Size(max = 256, message = "{user.name.size}")
    String name,
    @NotNull(message = "{user.timezone.notNull}")
    ZoneId timezone) {
}
