package matt.pass.mojaryba.domain.like;

import matt.pass.mojaryba.domain.fish.Fish;
import matt.pass.mojaryba.domain.fish.FishRepository;
import matt.pass.mojaryba.domain.user.User;
import matt.pass.mojaryba.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private FishRepository fishRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private LikeRepository likeRepositoryMock;
    @Captor
    private ArgumentCaptor<Like> likeCaptor;
    @InjectMocks
    private LikeService likeService;

    @Test
    public void shouldCreateNewLikeWhenLikeNotFound() {
        //given
        final Fish fish = new Fish();
        final User user = new User();
        String email = "example@example.com";

        when(userRepositoryMock.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));
        when(fishRepositoryMock.findById(1L)).thenReturn(Optional.of(fish));
        when(likeRepositoryMock.findByUser_EmailAndFish_Id(email, 1L)).thenReturn(Optional.empty());

        //when
        likeService.addOrUpdateLike(email, 1L);

        //then
        verify(likeRepositoryMock).save(likeCaptor.capture());
        final Like likeToSave = likeCaptor.getValue();
        assertThat(likeToSave.getFish()).isEqualTo(fish);
        assertThat(likeToSave.getUser()).isEqualTo(user);
        verify(likeRepositoryMock, never()).delete(ArgumentMatchers.any(Like.class));
    }

    @Test
    public void shouldDeleteLike() {
        //given
        final Fish fish = new Fish();
        final User user = new User();
        final Like like = new Like();
        String email = "example@example.com";

        when(userRepositoryMock.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));
        when(fishRepositoryMock.findById(1L)).thenReturn(Optional.of(fish));
        when(likeRepositoryMock.findByUser_EmailAndFish_Id(email, 1L)).thenReturn(Optional.of(like));

        //when
        likeService.addOrUpdateLike(email, 1L);

        //then
        verify(likeRepositoryMock).delete(like);
        verify(likeRepositoryMock, never()).save(ArgumentMatchers.any(Like.class));
    }

    @Test
    public void shouldThrowExceptionWhenFishNotFound() {
        //given
        String email = "example@example.com";

        when(fishRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> likeService.addOrUpdateLike(email, 1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        //given
        String email = "example@example.com";
        final Fish fish = new Fish();

        when(fishRepositoryMock.findById(1L)).thenReturn(Optional.of(fish));
        when(userRepositoryMock.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> likeService.addOrUpdateLike(email, 1L))
                .isInstanceOf(NoSuchElementException.class);
    }
}