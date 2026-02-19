package matt.pass.mojaryba.domain.user;

import matt.pass.mojaryba.domain.user.dto.UserAdministrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @InjectMocks
    private UserService userService;
    private final User user = new User();

    @BeforeEach
    void setUp() {
        user.setActivKey("123qwerty");
        user.setActiv(false);
    }

    @Test
    public void shouldCheckUserAndActiveAccount() {
        //given
        when(userRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        //when
        final boolean isActiv = userService.checkAndActivUserAccount(1L, "123qwerty");
        //then
        assertThat(user.isActiv()).isTrue();
        assertThat(isActiv).isTrue();
    }

    @Test
    public void shouldNotCheckedUserAndNotActiveAccount() {
        //given
        when(userRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        //when
        final boolean isActiv = userService.checkAndActivUserAccount(1L, "wrongActivKey");
        //then
        assertThat(user.isActiv()).isFalse();
        assertThat(isActiv).isFalse();
    }

    @Test
    public void shouldSetNewPassword() {
        //given
        when(userRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoderMock.encode("newPass")).thenReturn("newPassHash");
        //when
        final boolean passIsSet = userService.setNewPass(1L, "123qwerty", "newPass");
        //then
        assertThat(passIsSet).isTrue();
        assertThat(user.getPassword()).isEqualTo("newPassHash");
    }

    @Test
    public void shouldNotSetNewPassword() {
        //given
        when(userRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        //when
        final boolean passIsSet = userService.setNewPass(1L, "wrongActivKey", "newPass");
        //then
        assertThat(passIsSet).isFalse();
        assertThat(user.getPassword()).isEqualTo(null);
    }
    @Test
    public void shouldFindOneUser() {
        //given
        user.setNick("mateusz");
        user.setEmail("mateusz@gmail.com");
        when(userRepositoryMock.findAll()).thenReturn(List.of(user));
        //when
        //then
        final List<UserAdministrationDto> users = userService.findUsers("mat");
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.getFirst().getEmail()).isEqualTo(user.getEmail());
    }
    @Test
    public void shouldNotFindUser() {
        //given
        user.setNick("mateusz");
        user.setEmail("mateusz@gmail.com");
        when(userRepositoryMock.findAll()).thenReturn(List.of(user));
        //when
        //then
        final List<UserAdministrationDto> users = userService.findUsers("magda");
        assertThat(users.size()).isEqualTo(0);
    }

}