package be.g00glen00b.apps.mediminder.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.ZoneId;

public record CreateUserRequestDTO(
    @NotNull(message = "{user.email.notNull}")
    @Email(message = "{user.email.email}")
    @Size(max = 256, message = "{user.email.size}")
    String email,
    @Size(max = 256, message = "{user.name.size}")
    String name,
    @NotNull(message = "{user.password.notNull}")
    String password,
    @NotNull(message = "{user.timezone.notNull}")
    ZoneId timezone
) {
}
