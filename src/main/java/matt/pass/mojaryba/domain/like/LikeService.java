package matt.pass.mojaryba.domain.like;

import matt.pass.mojaryba.domain.fish.Fish;
import matt.pass.mojaryba.domain.fish.FishRepository;
import matt.pass.mojaryba.domain.user.User;
import matt.pass.mojaryba.domain.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final FishRepository fishRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, FishRepository fishRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.fishRepository = fishRepository;
    }

    public void addOrUpdateLike(String userEmail, long fishId) {
        final Fish fish = fishRepository.findById(fishId).orElseThrow();
        final User user = userRepository.findByEmailIgnoreCase(userEmail).orElseThrow();

        likeRepository.findByUser_EmailAndFish_Id(userEmail, fishId).ifPresentOrElse(
                likeRepository::delete,
                () -> likeRepository.save(new Like(fish, user))
        );

    }
}
