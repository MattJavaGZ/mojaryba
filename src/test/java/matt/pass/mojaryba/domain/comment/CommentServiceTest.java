package matt.pass.mojaryba.domain.comment;

import matt.pass.mojaryba.domain.fish.Fish;
import matt.pass.mojaryba.domain.fish.FishRepository;
import matt.pass.mojaryba.domain.user.User;
import matt.pass.mojaryba.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FishRepository fishRepository;
    @InjectMocks
    private CommentService commentService;
    @Captor
    private ArgumentCaptor<Comment> commentCaptor;

    @Test
    public void shouldAddCommentSuccessfully() {
        //given
        String userEmail = "example@moja-ryba.pl";
        long fishId = 1L;
        String commentText = "Nowy testowy komentarz";
        final User user = new User();
        final Fish fish = new Fish();

        when(fishRepository.findById(fishId)).thenReturn(Optional.of(fish));
        when(userRepository.findByEmailIgnoreCase(userEmail)).thenReturn(Optional.of(user));

        //when
        commentService.addComment(userEmail, fishId, commentText);

        //then
        verify(commentRepository).save(commentCaptor.capture());

        assertThat(commentCaptor.getValue().getContent()).isEqualTo(commentText);
        assertThat(commentCaptor.getValue().getFish()).isEqualTo(fish);
        assertThat(commentCaptor.getValue().getUser()).isEqualTo(user);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        //given
        String userEmail = "example@moja-ryba.pl";
        long fishId = 1L;
        String commentText = "Nowy testowy komentarz";
        final Fish fish = new Fish();

        when(fishRepository.findById(fishId)).thenReturn(Optional.of(fish));
        when(userRepository.findByEmailIgnoreCase(userEmail)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> commentService.addComment(userEmail, fishId, commentText))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldThrowExceptionWhenFishNotFound() {
        //given
        String userEmail = "example@moja-ryba.pl";
        String commentText = "Nowy testowy komentarz";
        long fishId = 1L;

        when(fishRepository.findById(fishId)).thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> commentService.addComment(userEmail, fishId, commentText))
                .isInstanceOf(NoSuchElementException.class);
    }
}
