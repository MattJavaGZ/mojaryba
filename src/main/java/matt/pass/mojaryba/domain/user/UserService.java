package matt.pass.mojaryba.domain.user;

import jakarta.transaction.Transactional;
import matt.pass.mojaryba.domain.user.dto.UserAdministrationDto;
import matt.pass.mojaryba.domain.user.dto.UserCredentialsDto;
import matt.pass.mojaryba.domain.user.dto.UserRegisterDto;
import matt.pass.mojaryba.infrastructure.config.CustomSecurityService;
import org.apache.commons.mail.EmailException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    public Optional<UserCredentialsDto> findActivUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .filter(User::isActiv)
                .map(UserMapper::map);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    public Optional<UserAdministrationDto> findUserAdministrationByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .map(UserMapper::mapToUserAdministration);
    }

    public User register(UserRegisterDto userRegisterDto) throws EmailException {
        final User userToSave = new User();
        userToSave.setEmail(userRegisterDto.getEmail().toLowerCase());
        userToSave.setNick(userRegisterDto.getNick());
        final String encodePassword = passwordEncoder.encode(userRegisterDto.getPassword());
        userToSave.setPassword(encodePassword);
        userToSave.setActiv(false);
        userToSave.setActivKey(generateActivKey());
        final UserRole defaultRole = userRoleRepository.findByName(CustomSecurityService.USER_ROLE).orElseThrow();
        userToSave.getRoles().add(defaultRole);
        return userRepository.save(userToSave);
    }

    private String generateActivKey() {
        return UUID.randomUUID().toString();
    }

    @Transactional
    public boolean checkAndActivUserAccount(long id, String activKey) {
        final User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.getActivKey().equals(activKey)) {
            user.setActiv(true);
            return true;
        } else return false;
    }

    public boolean chechExistByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public boolean chechExistByNick(String nick) {
        return userRepository.existsByNickIgnoreCase(nick);
    }

    @Transactional
    public boolean setNewPass(long id, String activKey, String newPassword) {
        final User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.getActivKey().equals(activKey)) {
            final String encodePassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodePassword);
            return true;
        }
        return false;
    }

    public List<UserAdministrationDto> findUsers(String findUser) {
        return userRepository.findAll().stream()
                .filter(user -> user.getNick().toLowerCase().contains(findUser.toLowerCase()) ||
                        user.getEmail().toLowerCase().contains(findUser.toLowerCase()))
                .map(UserMapper::mapToUserAdministration)
                .toList();
    }
    public boolean isBlocked(String userEmail) {
        final UserRole blockerRole = userRoleRepository.findByName(CustomSecurityService.BLOCKED_ROLE).orElseThrow();
        final User user = userRepository.findByEmailIgnoreCase(userEmail).orElseThrow();
        return user.getRoles().contains(blockerRole);
    }

    public List<User> findAllActiveUsersWithOutAuthor(User user){
        final List<User> users = userRepository.findAllByActivIsTrue();
        users.remove(user);
        return users;
    }
    @Transactional
    public void updateLastLoginDate(String email){
        userRepository.findByEmailIgnoreCase(email)
                .ifPresent(user -> user.setLastLoginDate(LocalDateTime.now()));
    }
    @Transactional
    public void updateLastActivDate(String email){
        userRepository.findByEmailIgnoreCase(email)
                .ifPresent(user -> user.setLastActivDate(LocalDateTime.now()));
    }
}
