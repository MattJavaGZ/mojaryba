package matt.pass.mojaryba.domain.fish;

import matt.pass.mojaryba.domain.fish.dto.FishDto;
import matt.pass.mojaryba.domain.like.Like;
import matt.pass.mojaryba.domain.rating.Rating;
import matt.pass.mojaryba.domain.type.FishType;
import matt.pass.mojaryba.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FishTopServiceTest {

    @Mock
    private FishRepository fishRepositoryMock;
    @InjectMocks
    private FishTopService fishTopService;
    private List<Fish> fishes = new ArrayList<>();

    @BeforeEach
    public void testData() {
        final FishType fishTypeKarp = new FishType();
        fishTypeKarp.setName("Karp");
        Fish fish1 = new Fish();
        fish1.setFishType(fishTypeKarp);
        fish1.setDescription("Karp złowiony na stawie");
        fish1.setTitle("");
        fish1.setFishingMethod("");
        fish1.setFishingSpot("");
        fish1.setBait("");
        fish1.setDateAdded(LocalDateTime.now());
        fish1.setRatings(List.of(new Rating(new Fish(), new User(), 3)));
        fish1.setWeight(22);

        final Like like = new Like();
        final User user1 = new User();
        user1.setNick("xxx");
        like.setUser(user1);
        final Like like2 = new Like();
        final User user2 = new User();
        user2.setNick("yyy");
        like2.setUser(user2);
        final Like like3 = new Like();
        final User user3 = new User();
        user3.setNick("zzz");
        like3.setUser(user3);
        fish1.setLikes(List.of(like, like2, like3));

        Fish fish2 = new Fish();
        fish2.setFishType(fishTypeKarp);
        fish2.setDescription("");
        fish2.setTitle("");
        fish2.setFishingMethod("");
        fish2.setFishingSpot("");
        fish2.setBait("");
        fish2.setDateAdded(LocalDateTime.now());
        fish2.setRatings(List.of(new Rating(new Fish(), new User(), 4)));
        fish2.setWeight(32);
        fish2.setLikes(List.of(like));

        final Fish fish3 = new Fish();
        final FishType fishTypeSum = new FishType();
        fishTypeSum.setName("Sum");
        fish3.setFishType(fishTypeSum);
        fish3.setDescription("");
        fish3.setTitle("");
        fish3.setFishingMethod("");
        fish3.setFishingSpot("");
        fish3.setBait("");
        fish3.setDateAdded(LocalDateTime.now());
        fish3.setRatings(Collections.emptyList());
        fish3.setWeight(10);
        fish3.setLikes(List.of(like2, like3));

        fishes.add(fish1);
        fishes.add(fish2);
        fishes.add(fish3);
    }

    @Test
    public void shouldReturnFish1AboveFish2ForRatingTop(){
        //given
        when(fishRepositoryMock.findAll()).thenReturn(fishes);

        //when
        //then
        final List<FishDto> top10RatedFishes = fishTopService.getTop10RatedFishes();
        assertThat(top10RatedFishes.size()).isEqualTo(2);
        assertThat(top10RatedFishes.getFirst().getRatingAvg()).isEqualTo(4);
        assertThat(top10RatedFishes.get(1).getRatingAvg()).isEqualTo(3);
    }

    @Test
    public void shouldReturnFish3AboveFish2AboveFish1ForLikedTop(){
        //givrn
        when(fishRepositoryMock.findAllByLikesIsNotNull()).thenReturn(fishes);

        //when
        //then
        final List<FishDto> top10LikedFishes = fishTopService.getTop10LikedFishes();
        assertThat(top10LikedFishes.size()).isEqualTo(3);
        assertThat(top10LikedFishes.getFirst().getLikedUserEmails().size()).isEqualTo(3);
        assertThat(top10LikedFishes.getLast().getLikedUserEmails().size()).isEqualTo(1);
    }

    @Test
    public void shouldReturnFish2AboveFish1ForBigestFishTop(){
        //given
        when(fishRepositoryMock.findAllByWeightGreaterThan(0)).thenReturn(fishes);

        //when
        //then
        final List<FishDto> top10BigestFishes = fishTopService.getTop10BigestFishes();
        assertThat(top10BigestFishes.size()).isEqualTo(3);
        assertThat(top10BigestFishes.getFirst().getWeight()).isEqualTo(32);
        assertThat(top10BigestFishes.getLast().getWeight()).isEqualTo(10);
    }
}