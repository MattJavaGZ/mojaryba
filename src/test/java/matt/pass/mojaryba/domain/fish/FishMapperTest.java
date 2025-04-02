package matt.pass.mojaryba.domain.fish;

import matt.pass.mojaryba.domain.fish.dto.FishDto;
import matt.pass.mojaryba.domain.like.Like;
import matt.pass.mojaryba.domain.rating.Rating;
import matt.pass.mojaryba.domain.type.FishType;
import matt.pass.mojaryba.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FishMapperTest {

    private Fish fish = new Fish();

    @BeforeEach
    public void setFishForTest() {
//        final Fish fish = new Fish();
        fish.setId(1L);
        fish.setTitle("a");
        fish.setDateAdded(LocalDateTime.now());
        fish.setDescription("a");
        fish.setWeight(1);
        fish.setLength(1);
        fish.setFishingMethod("a");
        fish.setBait("s");
        fish.setFishingSpot("A");
        FishType fishType = new FishType();
        fishType.setName("Karp");
        fish.setFishType(fishType);
        final User user = new User();
        user.setNick("New User");
        user.setEmail("a@a.pl");
        final User user1 = new User();
        user1.setNick("New User1");
        user1.setEmail("b@b.pl");
        final Rating rating = new Rating();
        rating.setFish(fish);
        rating.setUser(user);
        rating.setRating(5);
        final Rating rating1 = new Rating();
        rating1.setFish(fish);
        rating1.setUser(user1);
        rating1.setRating(3);
        fish.setRatings(List.of(rating, rating1));
        final Like like = new Like();
        like.setUser(user);
        like.setFish(fish);
        final Like like1 = new Like();
        like1.setUser(user1);
        like1.setFish(fish);
        fish.setLikes(List.of(like, like1));
        fish.setUser(user);
    }

    @Test
    public void shouldReturnRatingAvg4(){
        //when
        //then
        final FishDto fishDto = FishMapper.mapFishToFishDto(fish);
        assertThat(fishDto.getRatingAvg()).isEqualTo(4);
    }

    @Test
    public void shouldReturnRatingAvg0(){
        //given
        fish.setRatings(Collections.emptyList());

        //when
        //then
        final FishDto fishDto = FishMapper.mapFishToFishDto(fish);
        assertThat(fishDto.getRatingAvg()).isEqualTo(0);
    }

    @Test
    public void shouldReturnLikesListSize2(){
        //when
        //then
        final FishDto fishDto = FishMapper.mapFishToFishDto(fish);
        assertThat(fishDto.getLikedUserEmails().size()).isEqualTo(2);
        assertThat(fishDto.getLikedUserEmails()).contains("a@a.pl", "b@b.pl");
    }
    
    @Test
    public void shouldReturnCorrectAuthorNick() {
        //when
        //then
        final FishDto fishDto = FishMapper.mapFishToFishDto(fish);
        assertThat(fishDto.getAuthor()).isEqualTo("New User");
    }
    
    @Test
    public void shouldReturnCorrectAuthorForDeleteUser(){
        //given
        fish.setUser(null);
        //when
        //then
        final FishDto fishDto = FishMapper.mapFishToFishDto(fish);
        assertThat(fishDto.getAuthor()).isEqualTo("Usunięty");
    }

}