package matt.pass.mojaryba.infrastructure.email;

import matt.pass.mojaryba.domain.user.User;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailService {
    private final String url;

    public EmailService(@Value("${app.email.url}") String url) {
        this.url = url;
    }

    public void sendActivEmail(User user) throws EmailException {
        String title = "Aktywacja konta - Moja-Ryba.pl";
        String text = generateUserActivEmailText(user);
        sendEmail(user.getEmail(), title, text);
    }

    public void sendRemindPassEmail(User user) throws EmailException {
        String title = "Przypomnienia hasła - Moja-Ryba.pl";
        String text = generateRemindPassText(user);
        sendEmail(user.getEmail(), title, text);
    }

    public void sendContactEmail(String name, String email, String message) throws EmailException {
        String title = "Moja-Ryba.pl - wiadomość";
        String text = generateContactEmail(name, email, message);
        sendEmail("matekjava@onet.pl", title, text);
    }

    private void sendEmail(String userEmail, String title, String text) throws EmailException {
        SimpleEmail email = new SimpleEmail();
        email.setHostName("smtp.poczta.onet.pl");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("matekjava@onet.pl", "Jakieshaslo123@"));
        email.setSSLOnConnect(true);
        email.setFrom("matekjava@onet.pl");
        email.setSubject(title);
        email.setMsg(text);
        email.addTo(userEmail);
        email.send();
    }

    private String generateUserActivEmailText(User user) {
        return
                """
                        Dzień dobry %s,
                        
                        zarejestrowałeś konto w portalu Moja-Ryba.pl.
                        
                        Poniżej znajdziesz link służący do aktywacji konta:
                        %s
                        
                        Pozdrawiamy,
                        
                        """.formatted(user.getNick(), generateUserActivUrl(user));
    }

    private String generateUserActivUrl(User user) {
        return String.format("%s/aktywacja/%d?activKey=%s", url, user.getId(), user.getActivKey());
    }

    private String generateRemindPassText(User user) {
        return
                """
                        Dzień dobry %s,
                        
                        skorzystałeś z przypomnienia hasła.
                        
                        Poniżej znajdziesz link służący do ustawienia nowego hasła:
                        %s
                        
                        Proszę o zignorowanie wiadomości jeżeli nie to nie Ty korzystałeś z przypomnienia hasła.
                        
                        Pozdrawiamy,
                        
                        """.formatted(user.getNick(), generateRemindPassUrl(user));
    }

    private String generateRemindPassUrl(User user) {
        return String.format("%s/ustaw-nowe-haslo/%d?activKey=%s", url, user.getId(), user.getActivKey());
    }

    private String generateContactEmail(String name, String email, String message) {
        return """
                Wiadomość od użytkownika o imieniu: %s.
                Podany adres email: %s
                
                Treść wiadomości:
                %s
                
                """.formatted(name, email, message);
    }
}
