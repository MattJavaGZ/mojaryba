package matt.pass.mojaryba.domain.comment;

import matt.pass.mojaryba.domain.comment.dto.CommentDto;

public class CommentMapper {

    static CommentDto map (Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser() == null ? "Konto usunięte" : comment.getUser().getNick(),
                comment.getDateAdded()
        );
    }
}
