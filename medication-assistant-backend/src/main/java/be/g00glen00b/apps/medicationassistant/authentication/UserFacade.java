package be.g00glen00b.apps.medicationassistant.authentication;

import java.util.Optional;
import java.util.UUID;

public interface UserFacade {
    UserInfoDTO createUser(CreateUserRequestDTO request);
    Optional<UserAuthenticationInfoDTO> findByEmail(String email);
    Optional<UserInfoDTO> findById(UUID id);
}
