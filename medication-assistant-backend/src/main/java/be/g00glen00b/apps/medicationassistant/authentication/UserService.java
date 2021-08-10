package be.g00glen00b.apps.medicationassistant.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserInfoDTO createUser(CreateUserRequestDTO request) {
        validateUniqueEmailaddress(request.getEmail());
        String hash = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), request.getFirstName(), request.getLastName(), hash);
        User savedUser = repository.save(user);
        return new UserInfoDTO(savedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    private void validateUniqueEmailaddress(String email) {
        if (repository.existsByEmail(email)) {
            throw new InvalidUserException("There is already a user with this e-mail address");
        }
    }
}
