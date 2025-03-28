package matt.pass.mojaryba.domain.rating;

import matt.pass.mojaryba.domain.fish.Fish;
import matt.pass.mojaryba.domain.fish.FishRepository;
import matt.pass.mojaryba.domain.user.User;
import matt.pass.mojaryba.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepositoryMock;
    @Mock
    private FishRepository fishRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Captor
    private ArgumentCaptor<Rating> ratingCaptor;
    @InjectMocks
    private RatingService ratingService;

    @Test
    public void shouldUpdateExistingRatingOn3() {
        //given
        final Fish fish = new Fish();
        final User user = new User();
        String email = "example@example.com";
        Rating rating = new Rating();
        rating.setRating(5);

        when(ratingRepositoryMock.findByFish_IdAndUser_Email(anyLong(), anyString())).thenReturn(Optional.of(rating));
        when(fishRepositoryMock.findById(any())).thenReturn(Optional.of(fish));
        when(userRepositoryMock.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));

        //when
        ratingService.addOrUpdateRating(email, 1L, 3);
        //then

        verify(ratingRepositoryMock).save(ratingCaptor.capture());
        Assertions.assertThat(ratingCaptor.getValue().getRating()).isEqualTo(3);
        Assertions.assertThat(ratingCaptor.getValue().getFish()).isEqualTo(fish);
        Assertions.assertThat(ratingCaptor.getValue().getUser()).isEqualTo(user);

    }

    @Test
    public void shouldCreateNewRating(){
        //given
        final Fish fish = new Fish();
        final User user = new User();
        String email = "example@example.com";

        when(ratingRepositoryMock.findByFish_IdAndUser_Email(anyLong(), anyString())).thenReturn(Optional.empty());
        when(fishRepositoryMock.findById(any())).thenReturn(Optional.of(fish));
        when(userRepositoryMock.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));

        //when
        ratingService.addOrUpdateRating(email, 1L, 3);

        //then
        verify(ratingRepositoryMock).save(ratingCaptor.capture());
        Assertions.assertThat(ratingCaptor.getValue().getRating()).isEqualTo(3);
        Assertions.assertThat(ratingCaptor.getValue().getFish()).isEqualTo(fish);
        Assertions.assertThat(ratingCaptor.getValue().getUser()).isEqualTo(user);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound(){
        //given
        when(ratingRepositoryMock.findByFish_IdAndUser_Email(anyLong(), anyString())).thenReturn(Optional.empty());
        when(userRepositoryMock.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> ratingService.addOrUpdateRating("example@example.com", 1L, 3))
                .isInstanceOf(NoSuchElementException.class);
    }
    @Test
    public void shouldThrowExceptionWhenFishNotFound(){
        //given
        final User user = new User();
        when(ratingRepositoryMock.findByFish_IdAndUser_Email(anyLong(), anyString())).thenReturn(Optional.empty());
        when(userRepositoryMock.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(fishRepositoryMock.findById(any())).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> ratingService.addOrUpdateRating("example@example.com", 1L, 3))
                .isInstanceOf(NoSuchElementException.class);
    }

}