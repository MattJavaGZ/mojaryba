package matt.pass.mojaryba.infrastructure.email;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    @Async
    public void sendEmail(String userEmail, String title, String text) throws EmailException {
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

}
