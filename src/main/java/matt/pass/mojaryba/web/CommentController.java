package matt.pass.mojaryba.web;

import matt.pass.mojaryba.domain.comment.CommentService;
import matt.pass.mojaryba.domain.notification.NotificationService;
import matt.pass.mojaryba.web.util.RedirectUtils;
import org.apache.commons.mail.EmailException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final NotificationService notificationService;

    public CommentController(CommentService commentService, NotificationService notificationService) {
        this.commentService = commentService;
        this.notificationService = notificationService;
    }

    @PostMapping("/dodaj-komentarz")
    String addComment(@RequestParam String content, @RequestParam int id, Authentication authentication,
                      @RequestHeader (required = false) String referer) throws EmailException {
        final String userEmail = authentication.getName();
        commentService.addComment(userEmail, id, content);
        notificationService.saveCommentNotificationAndSendEmail(id);

        return "redirect:" + RedirectUtils.safeReturn(referer);
    }


}
