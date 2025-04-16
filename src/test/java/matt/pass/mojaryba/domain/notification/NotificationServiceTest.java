package matt.pass.mojaryba.domain.notification;

import matt.pass.mojaryba.domain.fish.Fish;
import matt.pass.mojaryba.domain.fish.FishRepository;
import matt.pass.mojaryba.domain.user.User;
import matt.pass.mojaryba.infrastructure.email.EmailService;
import org.apache.commons.mail.EmailException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepositoryMock;
    @Mock
    private FishRepository fishRepositoryMock;
    @Mock
    private EmailService emailServiceMock;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void shouldSetNotificationsOnRead() {
        //given
        final Notification notification1 = new Notification();
        notification1.setRead(false);
        final Notification notification2 = new Notification();
        notification2.setRead(false);
        final List<Notification> notifications = List.of(notification1, notification2);

        when(notificationRepositoryMock.findAllByUser_Email(anyString())).thenReturn(notifications);
        ArgumentCaptor<List<Notification>> captor = ArgumentCaptor.forClass(List.class);
        //when
        notificationService.setUserNotificationsOnRead("xxx@xxx.pl");
        //then
        verify(notificationRepositoryMock).saveAll(captor.capture());
        final List<Notification> result = captor.getValue();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getFirst().isRead()).isTrue();
        assertThat(result.getLast().isRead()).isTrue();
    }

    @Test
    public void shouldNotSaveCommentNotificationAndSendEmail() throws EmailException {
        //given
        final User user = new User();
        user.setEmail("example@example.com");
        final Fish fish = new Fish();
        fish.setUser(user);
        //when
        when(fishRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(fish));
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("example@example.com");

        notificationService.saveCommentNotificationAndSendEmail(1L);
        //them
        verify(emailServiceMock, Mockito.never()).sendCommentNotificationEmail(fish.getUser(), fish);
    }

    @Test
    public void shouldSaveRatingNotificationAndSendEmail() throws EmailException {
        //given
        final User user = new User();
        user.setEmail("example@example.com");
        final Fish fish = new Fish();
        fish.setUser(user);
        //when
        when(fishRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(fish));
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("example222@example.com");

        notificationService.saveRatingNotificationAndSendEmail(1L);
        //them
        verify(emailServiceMock).sendRatingNotificationEmail(fish.getUser(), fish);
    }

}