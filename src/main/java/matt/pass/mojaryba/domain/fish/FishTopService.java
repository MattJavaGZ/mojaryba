package matt.pass.mojaryba.domain.fish;

import matt.pass.mojaryba.domain.fish.dto.FishDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FishTopService {
    private final FishRepository fishRepository;

    public FishTopService(FishRepository fishRepository) {
        this.fishRepository = fishRepository;
    }

    public List<FishDto> getTop10RatedFishes() {
        return fishRepository.findAll().stream()
                .map(FishMapper::mapFishToFishDto)
                .filter(fish -> fish.getRatingAvg() > 0)
                .sorted((o1, o2) -> -Double.compare(o1.getRatingAvg(), o2.getRatingAvg()))
                .limit(10)
                .toList();
    }
    public List<FishDto> getTop10LikedFishes() {
        return fishRepository.findAllByLikesIsNotNull().stream()
                .map(FishMapper::mapFishToFishDto)
                .sorted((o1, o2) -> -Integer.compare(o1.getLikedUserEmails().size(), o2.getLikedUserEmails().size()))
                .limit(10)
                .toList();
    }
    public List<FishDto> getTop10BigestFishes() {
        return fishRepository.findAllByWeightGreaterThan(0).stream()
                .map(FishMapper::mapFishToFishDto)
                .sorted((o1, o2) -> -Double.compare(o1.getWeight(), o2.getWeight()))
                .limit(10)
                .toList();
    }
}
