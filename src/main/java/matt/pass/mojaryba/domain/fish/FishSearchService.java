package matt.pass.mojaryba.domain.fish;

import matt.pass.mojaryba.domain.fish.dto.FishDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FishSearchService {
    private final FishRepository fishRepository;

    public FishSearchService(FishRepository fishRepository) {
        this.fishRepository = fishRepository;
    }

    public List<FishDto> searchFishes(String userSearch) {
        final List<Fish> allFishes = fishRepository.findAll();
        return search(allFishes, userSearch);
    }

    public List<FishDto> searchInUserFishes(String userEmail, String userSearch) {
        final List<Fish> allFishesByUser = fishRepository.findAllByUser_Email(userEmail);
        return search(allFishesByUser, userSearch);
    }

    private List<FishDto> search(List<Fish> fishes, String userSearch) {
        return fishes.stream()
                .map(FishMapper::mapFishToFishDto)
                .filter(fish -> searchInFish(fish, userSearch))
                .sorted()
                .toList();
    }

    private static boolean searchInFish(FishDto fish, String userSearch) {
        final String[] userSearchSplit = userSearch.toLowerCase().split(" ");

        for (String text : userSearchSplit) {
            if (fish.getTitle().toLowerCase().contains(text) ||
                    fish.getDescription().toLowerCase().contains(text) ||
                    fish.getFishingMethod().toLowerCase().contains(text) ||
                    fish.getFishingSpot().toLowerCase().contains(text) ||
                    fish.getFishType().toLowerCase().contains(text) ||
                    fish.getBait().toLowerCase().contains(text))
                return true;
        }
        return false;
    }

}
