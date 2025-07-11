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
                .sorted(FishTopService::topRatedCompare)
                .limit(10)
                .toList();
    }


    public List<FishDto> getTop10LikedFishes() {
        return fishRepository.findAllByLikesIsNotNull().stream()
                .map(FishMapper::mapFishToFishDto)
                .sorted(FishTopService::topLikedCompare)
                .limit(10)
                .toList();
    }


    public List<FishDto> getTop10BigestFishes() {
        return fishRepository.findAllByWeightGreaterThan(0).stream()
                .map(FishMapper::mapFishToFishDto)
                .sorted(FishTopService::topBigestCompare)
                .limit(10)
                .toList();
    }

    private static int topRatedCompare(FishDto o1, FishDto o2) {
        final int ratingCompare = -Double.compare(o1.getRatingAvg(), o2.getRatingAvg());
        if (ratingCompare != 0) {
            return ratingCompare;
        }
        final int likesCompare = -Integer.compare(o1.getLikedUserEmails().size(), o2.getLikedUserEmails().size());
        if (likesCompare != 0) {
            return likesCompare;
        }
        return -o1.getDateAdded().compareTo(o2.getDateAdded());

    }

    private static int topLikedCompare(FishDto o1, FishDto o2) {
        final int likesCompare = -Integer.compare(o1.getLikedUserEmails().size(), o2.getLikedUserEmails().size());
        if (likesCompare != 0) {
            return likesCompare;
        }
        final int ratingCompare = -Double.compare(o1.getRatingAvg(), o2.getRatingAvg());
        if (ratingCompare != 0) {
            return ratingCompare;
        }
        return -o1.getDateAdded().compareTo(o2.getDateAdded());
    }

    private static int topBigestCompare(FishDto o1, FishDto o2) {
        final int weightCompare = -Double.compare(o1.getWeight(), o2.getWeight());
        if (weightCompare != 0) {
            return weightCompare;
        }
        return -o1.getDateAdded().compareTo(o2.getDateAdded());
    }
}
