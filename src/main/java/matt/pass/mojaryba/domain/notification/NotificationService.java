package matt.pass.mojaryba.domain.notification;

import matt.pass.mojaryba.domain.fish.Fish;
import matt.pass.mojaryba.domain.fish.FishRepository;
import matt.pass.mojaryba.domain.notification.dto.NotificationDto;
import matt.pass.mojaryba.infrastructure.email.EmailService;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NotificationService {

    private final static String COMMENT_DESCRIPTION = "Ktoś dodał komentarz to Twojej ryby! Sprawdź! ";
    private final static String RATING_DESCRIPTION = "Ktoś ocenił Twoją rybę! Sprawdź! ";
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private NotificationRepository notificationRepository;
    private FishRepository fishRepository;
    private EmailService emailService;

    public NotificationService(NotificationRepository notificationRepository, FishRepository fishRepository, EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.fishRepository = fishRepository;
        this.emailService = emailService;
    }

    public List<NotificationDto> getAllNotificationsForUser(String userEmail) {
       return notificationRepository.findAllByUser_Email(userEmail).reversed().stream()
                .map(NotificationMapper::mapNotificationToNotificationDto)
                .toList();
    }

    public int getUnreadNotificationsCount() {
        final String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return (int) getAllNotificationsForUser(userEmail).stream()
                .filter(notification -> !notification.isRead())
                .count();
    }
    public void setUserNotificationsOnRead(String email) {
        final List<Notification> notificationsToUpdate = notificationRepository.findAllByUser_Email(email).stream()
                .peek(notification -> notification.setRead(true))
                .toList();

        notificationRepository.saveAll(notificationsToUpdate);
    }

    public void saveCommentNotificationAndSendEmail(long fishId) throws EmailException {
        final Fish fish = fishRepository.findById(fishId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (fish.getUser() != null && !verifiUserAndAuthor(fish)) {
            saveNotification(fish, COMMENT_DESCRIPTION);
            emailService.sendCommentNotificationEmail(fish.getUser(), fish);
        }
    }

    public void saveRatingNotificationAndSendEmail(long fishId) throws EmailException {
        final Fish fish = fishRepository.findById(fishId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (fish.getUser() != null && !verifiUserAndAuthor(fish)) {
            saveNotification(fish, RATING_DESCRIPTION);
            emailService.sendRatingNotificationEmail(fish.getUser(), fish);
        }
    }

    private void saveNotification (Fish fish, String description) {
        Notification notification = new Notification();
        notification.setUser(fish.getUser());
        notification.setFish(fish);
        notification.setRead(false);
        notification.setDescription(description);

        notificationRepository.save(notification);
    }
    private boolean verifiUserAndAuthor(Fish fish) {
        final String authorEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return fish.getUser().getEmail().equals(authorEmail);
    }


}
