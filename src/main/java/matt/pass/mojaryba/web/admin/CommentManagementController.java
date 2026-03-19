package matt.pass.mojaryba.web.admin;

import matt.pass.mojaryba.domain.comment.CommentService;
import matt.pass.mojaryba.web.util.RedirectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class CommentManagementController {
    private final CommentService commentService;

    public CommentManagementController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/admin/usun-komentarz/{id}")
    String deleteComment(@PathVariable long id, @RequestHeader (required = false) String referer) {
        commentService.deleteCommentById(id);
        return "redirect:" + RedirectUtils.safeReturn(referer);
    }

}
