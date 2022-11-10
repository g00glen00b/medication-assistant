package be.g00glen00b.apps.mediminder.user.implementation;

import be.g00glen00b.apps.mediminder.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
class UserService implements UserFacade, UserDetailsService {
    public static final ZoneId DEFAULT_TIMEZONE = ZoneId.of("UTC");
    private final UserEntityRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    @Override
    @Transactional
    public UserInfoDTO create(@Valid CreateUserRequestDTO request) {
        validateUniqueEmailAddress(request.email());
        String hash = passwordEncoder.encode(request.password());
        UserEntity user = UserEntity.of(request.email(), request.name(), hash, request.timezone());
        UserEntity savedUser = repository.save(user);
        return UserInfoDTO.ofEntity(savedUser);
    }

    @Override
    public UserInfoDTO findById(UUID id) {
        return repository
            .findById(id)
            .map(UserInfoDTO::ofEntity)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Transactional
    public UserInfoDTO update(UUID id, @Valid UpdateUserRequestDTO request) {
        UserEntity entity = findByIdOrThrowException(id);
        entity.setName(request.name());
        entity.setTimezone(request.timezone());
        return UserInfoDTO.ofEntity(entity);
    }

    @Override
    @Transactional
    public UserInfoDTO updateCredentials(UUID id, @Valid UpdateCredentialsRequestDTO request) {
        UserEntity entity = findByIdOrThrowException(id);
        validatePasswordsMatch(entity, request.oldPassword());
        entity.setPassword(passwordEncoder.encode(request.newPassword()));
        return UserInfoDTO.ofEntity(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
            .findByEmail(username)
            .map(UserAuthentication::ofEntity)
            .orElseThrow(() -> new UsernameNotFoundException("There is no such user"));
    }

    @Override
    public Collection<String> findAvailableTimezones() {
        return ZoneId
            .getAvailableZoneIds()
            .stream()
            .sorted()
            .toList();
    }

    @Override
    public LocalDateTime calculateTodayForUser(UUID id) {
        ZoneId timezone = findUserTimezoneOrDummy(id);
        return Instant.now(clock).atZone(timezone).toLocalDateTime();
    }

    private ZoneId findUserTimezoneOrDummy(UUID id) {
        return repository
            .findById(id)
            .map(UserEntity::getTimezone)
            .orElse(DEFAULT_TIMEZONE);
    }

    private void validateUniqueEmailAddress(String email) {
        if (repository.existsByEmail(email)) {
            throw new InvalidUserException("There is already a user with this e-mail address");
        }
    }

    private UserEntity findByIdOrThrowException(UUID id) {
        return repository
            .findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    private void validatePasswordsMatch(UserEntity entity, String oldPassword) {
        if (!passwordEncoder.matches(oldPassword, entity.getPassword())) {
            throw new InvalidUserException("The password is not correct");
        }
    }
}
