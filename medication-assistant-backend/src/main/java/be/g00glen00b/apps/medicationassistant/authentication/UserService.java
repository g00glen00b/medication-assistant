package be.g00glen00b.apps.medicationassistant.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
class UserService implements UserDetailsService, UserFacade {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserInfoDTO createUser(@Valid CreateUserRequestDTO request) {
        validateUniqueEmailaddress(request.getEmail());
        String hash = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), request.getFirstName(), request.getLastName(), hash);
        User savedUser = repository.save(user);
        return new UserInfoDTO(savedUser);
    }

    @Override
    public Optional<UserAuthenticationInfoDTO> findByEmail(String email) {
        return repository
            .findByEmail(email)
            .map(UserAuthenticationInfoDTO::new);
    }

    @Override
    public Optional<UserInfoDTO> findById(UUID id) {
        return repository
            .findById(id)
            .map(UserInfoDTO::new);
    }

    @Override
    public UserAuthenticationInfoDTO loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("There is no such user"));
    }

    private void validateUniqueEmailaddress(String email) {
        if (repository.existsByEmail(email)) {
            throw new InvalidUserException("There is already a user with this e-mail address");
        }
    }
}
