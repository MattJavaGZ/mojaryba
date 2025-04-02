package matt.pass.mojaryba.domain.comment;

import matt.pass.mojaryba.domain.comment.dto.CommentDto;
import matt.pass.mojaryba.domain.user.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentMapperTest {

    @Test
    public void shouldReturnNick(){
        //given
        final Comment comment = new Comment();
        final User user = new User();
        user.setNick("Testowy Nick");
        comment.setUser(user);

        //when
        final CommentDto commentDto = CommentMapper.map(comment);

        //then
        assertThat(commentDto.getUserNick()).isEqualTo(user.getNick());
    }

    @Test
    public void shouldReturnUserDelete() {
        //given
        final Comment comment = new Comment();

        //when
        final CommentDto commentDto = CommentMapper.map(comment);

        //then
        assertThat(commentDto.getUserNick()).isEqualTo("Konto usunięte");
    }

}