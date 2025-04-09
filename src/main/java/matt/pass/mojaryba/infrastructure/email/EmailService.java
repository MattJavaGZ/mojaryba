package matt.pass.mojaryba.infrastructure.email;

import matt.pass.mojaryba.domain.fish.Fish;
import matt.pass.mojaryba.domain.user.User;
import org.apache.commons.mail.EmailException;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private EmailSender emailSender;
    private EmailTemplate emailTemplate;

    public EmailService(EmailSender emailSender, EmailTemplate emailTemplate) {
        this.emailSender = emailSender;
        this.emailTemplate = emailTemplate;
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
        String title = "Nowy komentarz - Moja-Ryba.pl";
        String text = emailTemplate.generateCommentNotificationEmail(user, fish);
        emailSender.sendEmail(user.getEmail(), title, text);
    }

    public void sendRatingNotificationEmail(User user, Fish fish) throws EmailException {
        String title = "Nowa ocena - Moja-Ryba.pl";
        String text = emailTemplate.generateRatingNotificationEmail(user, fish);
        emailSender.sendEmail(user.getEmail(), title, text);
    }




}
