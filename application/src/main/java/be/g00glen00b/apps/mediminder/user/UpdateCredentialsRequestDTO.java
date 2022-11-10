package be.g00glen00b.apps.mediminder.user;

import jakarta.validation.constraints.NotNull;

public record UpdateCredentialsRequestDTO(
    @NotNull(message = "{user.oldPassword.notNull}") String oldPassword,
    @NotNull(message = "{user.newPassword.notNull}") String newPassword) {
}
