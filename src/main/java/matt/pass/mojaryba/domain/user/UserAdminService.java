package matt.pass.mojaryba.domain.user;

import jakarta.transaction.Transactional;
import matt.pass.mojaryba.domain.user.dto.UserAdministrationDto;
import matt.pass.mojaryba.domain.user.exceptions.NickExistsException;
import matt.pass.mojaryba.infrastructure.config.CustomSecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserAdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    public UserAdminService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                            UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }
    public void deleteUserByEmail (String email){
        final Optional<User> userToDelete = userRepository.findByEmailIgnoreCase(email);
        userToDelete.ifPresent(userRepository::delete);
    }

    public UserAdministrationDto findAdministrationUserById(long id) {
        return userRepository.findById(id)
                .map(UserMapper::mapToUserAdministration)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void adminEditUserNickById(String nick, long userId) {
        final boolean nickExists = userRepository.existsByNickIgnoreCase(nick);
        if (nickExists) {
            throw new NickExistsException(nick);
        }
        final User user = userRepository.findById(userId).orElseThrow();
        user.setNick(nick);
    }

    @Transactional
    public void adminEditUserNickByEmail(String nick, String userEmail) {
        final boolean nickExists = userRepository.existsByNickIgnoreCase(nick);
        if (nickExists) {
            throw new NickExistsException(nick);
        }
        final User user = userRepository.findByEmailIgnoreCase(userEmail).orElseThrow();
        user.setNick(nick);
    }

    @Transactional
    public void adminEditUserPassById(String password, long userId) {
        final User user = userRepository.findById(userId).orElseThrow();
        setNewPass(user, password);
    }

    @Transactional
    public void adminEditUserPassByEmail(String password, String userEmail) {
        final User user = userRepository.findByEmailIgnoreCase(userEmail).orElseThrow();
        setNewPass(user, password);
    }

    private void setNewPass(User user, String password) {
        final String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);
    }

    @Transactional
    public void adminEditUserEmail(String email, long userId) {
        final User user = userRepository.findById(userId).orElseThrow();
        user.setEmail(email);
    }

    @Transactional
    public void deactivateOrActivateUser(long userId) {
        final User user = userRepository.findById(userId).orElseThrow();
        user.setActiv(!user.isActiv());
    }

    @Transactional
    public void blockUser(long userId) {
        final User user = userRepository.findById(userId).orElseThrow();
        user.getRoles().clear();
        final UserRole blockRole = userRoleRepository.findByName(CustomSecurityService.BLOCKED_ROLE).orElseThrow();
        user.getRoles().add(blockRole);
    }

    @Transactional
    public void unblockUser(long userId) {
        final User user = userRepository.findById(userId).orElseThrow();
        user.getRoles().clear();
        final UserRole userRole = userRoleRepository.findByName(CustomSecurityService.USER_ROLE).orElseThrow();
        user.getRoles().add(userRole);
    }

}
