package matt.pass.mojaryba.infrastructure.email;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    private final String emailLogin;
    private final String emailPassword;

    public EmailSender(@Value("${app.email.login}") String emailLogin,
                       @Value("${app.email.password}") String emailPassword) {
        this.emailLogin = emailLogin;
        this.emailPassword = emailPassword;
    }

    public void sendEmail(String userEmail, String title, String text) throws EmailException {
        SimpleEmail email = new SimpleEmail();
        email.setHostName("smtp.poczta.onet.pl");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator(emailLogin, emailPassword));
        email.setSSLOnConnect(true);
        email.setFrom(emailLogin);
        email.setSubject(title);
        email.setMsg(text);
        email.addTo(userEmail);
        email.send();
    }

}
