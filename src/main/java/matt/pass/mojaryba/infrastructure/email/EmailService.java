package matt.pass.mojaryba.infrastructure.email;

import matt.pass.mojaryba.domain.email.EmailSettings;
import matt.pass.mojaryba.domain.email.EmailSettingsService;
import matt.pass.mojaryba.domain.fish.Fish;
import matt.pass.mojaryba.domain.user.User;
import org.apache.commons.mail.EmailException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private final EmailSender emailSender;
    private final EmailTemplate emailTemplate;
    private final EmailSettingsService emailSettingsService;

    public EmailService(EmailSender emailSender, EmailTemplate emailTemplate, EmailSettingsService emailSettingsService) {
        this.emailSender = emailSender;
        this.emailTemplate = emailTemplate;
        this.emailSettingsService = emailSettingsService;
    }

    public void sendActivEmail(User user) throws EmailException {
        String title = "Aktywacja konta - Moja-Ryba.pl";
        String text = emailTemplate.generateUserActivEmailText(user);
        emailSender.sendEmail(user.getEmail(), title, text);
    }

    public void sendRemindPassEmail(User user) throws EmailException {
        String title = "Przypomnienia hasła - Moja-Ryba.pl";
        String text = emailTemplate.generateRemindPassText(user);
        emailSender.sendEmail(user.getEmail(), title, text);
    }

    public void sendContactEmail(String name, String email, String message) throws EmailException {
        String title = "Moja-Ryba.pl - wiadomość";
        String text = emailTemplate.generateContactEmail(name, email, message);
        emailSender.sendEmail("matekjava@onet.pl", title, text);
    }

    public void sendCommentNotificationEmail(User user, Fish fish) throws EmailException {
        if (!emailSettingsService.verifyMailingStatus(EmailSettings.EmailType.COMMENT)) {
            return;
        }
        String title = "Nowy komentarz - Moja-Ryba.pl";
        String text = emailTemplate.generateCommentNotificationEmail(user, fish);
        emailSender.sendEmail(user.getEmail(), title, text);
    }

    public void sendRatingNotificationEmail(User user, Fish fish) throws EmailException {
        if (!emailSettingsService.verifyMailingStatus(EmailSettings.EmailType.RATING)) {
            return;
        }
        String title = "Nowa ocena - Moja-Ryba.pl";
        String text = emailTemplate.generateRatingNotificationEmail(user, fish);
        emailSender.sendEmail(user.getEmail(), title, text);
    }

    public void sendEmailsAboutNewFishesToAllUsers(List<User> users, long fishId) throws EmailException {
        if (!emailSettingsService.verifyMailingStatus(EmailSettings.EmailType.NEW_FISH)) {
            return;
        }
        String tittle = "Nowy okaz na stronie Moja-Ryba.pl";

        for (User user : users) {
            String text = emailTemplate.generateEmailAboutNewFish(user, fishId);
            emailSender.sendEmail(user.getEmail(), tittle, text);
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Przerwano wysyłke maili, e");
                break;
            }
        }
    }


}
