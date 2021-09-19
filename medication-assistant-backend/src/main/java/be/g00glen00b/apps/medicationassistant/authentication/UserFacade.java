package be.g00glen00b.apps.medicationassistant.authentication;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

public interface UserFacade {
    UserInfoDTO createUser(@Valid CreateUserRequestDTO request);
    Optional<UserAuthenticationInfoDTO> findByEmail(String email);
    Optional<UserInfoDTO> findById(UUID id);
}
