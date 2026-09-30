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
    private final RedirectUtils redirectUtils;

    public CommentManagementController(CommentService commentService, RedirectUtils redirectUtils) {
        this.commentService = commentService;
        this.redirectUtils = redirectUtils;
    }

    @GetMapping("/admin/usun-komentarz/{id}")
    String deleteComment(@PathVariable long id, @RequestHeader (required = false) String referer) {
        commentService.deleteCommentById(id);
        return "redirect:" + redirectUtils.safeReturn(referer);
    }

}
