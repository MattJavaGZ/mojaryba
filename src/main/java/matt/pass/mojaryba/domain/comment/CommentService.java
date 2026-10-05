package matt.pass.mojaryba.domain.comment;

import matt.pass.mojaryba.domain.comment.dto.CommentDto;
import matt.pass.mojaryba.domain.fish.Fish;
import matt.pass.mojaryba.domain.fish.FishRepository;
import matt.pass.mojaryba.domain.user.User;
import matt.pass.mojaryba.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final FishRepository fishRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, FishRepository fishRepository, UserRepository userService) {
        this.commentRepository = commentRepository;
        this.fishRepository = fishRepository;
        this.userRepository = userService;
    }

    public void addComment(String userEmail, long fishId, String commentContent) {
        final Fish fish = fishRepository.findById(fishId).orElseThrow();
        final User user = userRepository.findByEmailIgnoreCase(userEmail).orElseThrow();
        final Comment commentToSave = new Comment(user, fish, commentContent);
        commentRepository.save(commentToSave);
    }

    public List<CommentDto> getCommentsForFish(long fishId) {
        return commentRepository.findAllByFish_Id(fishId).stream()
                .sorted()
                .map(CommentMapper::map)
                .toList();
    }
    public void deleteCommentById(long id) {
        commentRepository.deleteById(id);
    }
}
