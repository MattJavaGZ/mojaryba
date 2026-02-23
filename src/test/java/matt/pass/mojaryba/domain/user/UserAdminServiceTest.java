package matt.pass.mojaryba.domain.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAdminServiceTest {

    private final static String BLOCKED = "BLOCKED_ROLE";
    private final static String USER = "USER_ROLE";

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private UserRoleRepository userRoleRepositoryMock;
    @InjectMocks
    private UserAdminService userAdminService;

    @Test
    public void shouldThrowExceptionForFindAdministrationUserNotFound() {
        //given
        when(userRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThatThrownBy(() -> userAdminService.findAdministrationUserById(1L))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    public void shouldThrowExceptionForEditUserNickUserNotFound() {
        //given
        when(userRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThatThrownBy(() -> userAdminService.adminEditUserNickById("new nick", 1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldActiveUserAccount() {
        //given
        final User user = new User();
        user.setActiv(false);
        when(userRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        //when
        userAdminService.deactivateOrActivateUser(1L);
        //then
        Assertions.assertThat(user.isActiv()).isTrue();
    }

    @Test
    public void shouldDeactivateUserAccount() {
        //given
        final User user = new User();
        user.setActiv(true);
        when(userRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        //when
        userAdminService.deactivateOrActivateUser(1L);
        //then
        Assertions.assertThat(user.isActiv()).isFalse();
    }

    @Test
    public void shouldBlochUser() {
        //given
        final User user = new User();
        final UserRole userRole = new UserRole();
        userRole.setName(BLOCKED);
        when(userRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        when(userRoleRepositoryMock.findByName(ArgumentMatchers.anyString())).thenReturn(Optional.of(userRole));
        //when
        userAdminService.blockUser(1L);
        //then
        Assertions.assertThat(user.getRoles().size()).isEqualTo(1);
        Assertions.assertThat(user.getRoles().contains(userRole)).isTrue();
    }

    @Test
    public void shpuldUnBlockUser() {
        //given
        final User user = new User();
        final UserRole userRole = new UserRole();
        userRole.setName(USER);
        when(userRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        when(userRoleRepositoryMock.findByName(ArgumentMatchers.anyString())).thenReturn(Optional.of(userRole));
        //when
        userAdminService.unblockUser(1L);
        //then
        Assertions.assertThat(user.getRoles().size()).isEqualTo(1);
        Assertions.assertThat(user.getRoles().contains(userRole)).isTrue();
    }
}