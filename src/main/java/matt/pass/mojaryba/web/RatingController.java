package matt.pass.mojaryba.web;

import matt.pass.mojaryba.domain.notification.NotificationService;
import matt.pass.mojaryba.domain.rating.RatingService;
import matt.pass.mojaryba.web.util.RedirectUtils;
import org.apache.commons.mail.EmailException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RatingController {
    private final RatingService ratingService;
    private final NotificationService notificationService;

    public RatingController(RatingService ratingService, NotificationService notificationService) {
        this.ratingService = ratingService;
        this.notificationService = notificationService;
    }

    @PostMapping("/ocen-rybe")
    String addRating(@RequestParam long fishId, @RequestParam int rating, Authentication authentication,
                     @RequestHeader (required = false) String referer) throws EmailException {
        final String userEmail = authentication.getName();
        ratingService.addOrUpdateRating(userEmail, fishId, rating);
        notificationService.saveRatingNotificationAndSendEmail(fishId);

        return "redirect:" + RedirectUtils.safeReturn(referer);
    }
}
