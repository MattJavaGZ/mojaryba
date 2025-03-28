package matt.pass.mojaryba.domain.favorite;

import matt.pass.mojaryba.domain.fish.Fish;
import matt.pass.mojaryba.domain.fish.FishRepository;
import matt.pass.mojaryba.domain.fish.dto.FishDto;
import matt.pass.mojaryba.domain.type.FishType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FishRepository fishRepositoryMock;
    @InjectMocks
    private FavoriteService favoriteService;

    @Test
    public void shouldReturnEmptyList() {
        //given
        String favorite = "";
        //when
        //then
        final Set<FishDto> favoritesList = favoriteService.getFavoritesList(favorite);
        assertThat(favoritesList).isEmpty();
    }

    @Test
    public void shouldReturnTwoFishesInFavoritesList() {
        //given
        final String favorite = "1;2;";

        final Fish fish1 = new Fish();
        final FishType karp = new FishType();
        karp.setName("Karp");
        final FishType sum = new FishType();
        sum.setName("sum");
        fish1.setFishType(karp);
        final Fish fish2 = new Fish();
        fish2.setFishType(sum);

        when(fishRepositoryMock.findById(Long.valueOf("1"))).thenReturn(Optional.of(fish1));
        when(fishRepositoryMock.findById(Long.valueOf("2"))).thenReturn(Optional.of(fish2));

        //when
        final Set<FishDto> favoritesList = favoriteService.getFavoritesList(favorite);

        //then
        assertThat(favoritesList).hasSize(2);
    }

    @Test
    public void shouldDeleteFishIdWithFavoritesList() {
        //given
        String favorite = " 1; 2; 5; 15; 255; 555";
        
        //when
        //then
        final String favoriteAfterDelete = favoriteService.deleteWithFavorite(favorite, 5L);
        assertThat(favoriteAfterDelete).doesNotContain(" 5;" );
    }

    @Test
    public void shouldAddFishIdToFavoritesList() {
        //given
        String favorite = " 1; 2; 5; 15; 255; 555;";
        long id = 333;

        //when then
        final String result = favoriteService.addToFavorite(favorite, id);
        assertThat(result).contains(favorite);
        assertThat(result).contains(" " + id + ";");
    }
  
}