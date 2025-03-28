package matt.pass.mojaryba.domain.fish;

import matt.pass.mojaryba.domain.fish.dto.FishDto;
import matt.pass.mojaryba.domain.type.FishType;
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
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FishSearchServiceTest {

    @Mock
    private FishRepository fishRepositoryMock;
    @InjectMocks
    private FishSearchService fishSearchService;
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
        Fish fish2 = new Fish();
        fish2.setFishType(fishTypeKarp);
        fish2.setDescription("");
        fish2.setTitle("");
        fish2.setFishingMethod("");
        fish2.setFishingSpot("");
        fish2.setBait("");
        fish2.setDateAdded(LocalDateTime.now());
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

        fishes.add(fish1);
        fishes.add(fish2);
        fishes.add(fish3);
    }

    @Test
    public void shouldReturnTwoFishesForUserSearch(){
        //given
        when(fishRepositoryMock.findAll()).thenReturn(fishes);
        final Fish fish1 = fishes.getFirst();
        final Fish fish2 = fishes.get(1);

        //when
        //then
        final List<FishDto> fishes = fishSearchService.searchFishes("karp");
        assertThat(fishes.size()).isEqualTo(2);
        assertThat(fishes).contains(FishMapper.mapFishToFishDto(fish1), FishMapper.mapFishToFishDto(fish2));
    }

    @Test
    public void shouldReturnEmptyListForUserFishes(){
        //given
        when(fishRepositoryMock.findAllByUser_Email(anyString())).thenReturn(Collections.emptyList());

        //when
        //then
        final List<FishDto> fishesByUser = fishSearchService.searchInUserFishes("example@example.com", "karp");
        assertThat(fishesByUser).isEmpty();
    }
}