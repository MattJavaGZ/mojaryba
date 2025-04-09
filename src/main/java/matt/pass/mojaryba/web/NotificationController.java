package matt.pass.mojaryba.web;

import matt.pass.mojaryba.domain.notification.NotificationService;
import matt.pass.mojaryba.domain.notification.dto.NotificationDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NotificationController {

    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/powiadomienia")
    String getNotifications(Model model, Authentication authentication) {
        if (authentication != null) {
            final String userEmail = authentication.getName();
            final List<NotificationDto> notificationsForUser = notificationService.getAllNotificationsForUser(userEmail);
            model.addAttribute("notifications", notificationsForUser);
            notificationService.setUserNotificationsOnRead(userEmail);
        }
        return "notification";
    }
}
