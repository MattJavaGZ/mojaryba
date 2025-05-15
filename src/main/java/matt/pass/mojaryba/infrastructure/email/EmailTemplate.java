package matt.pass.mojaryba.infrastructure.email;

import matt.pass.mojaryba.domain.fish.Fish;
import matt.pass.mojaryba.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailTemplate {

    private final String url;

    public EmailTemplate(@Value("${app.email.url}") String url) {
        this.url = url;
    }

    String generateUserActivEmailText(User user) {
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

    String generateRemindPassText(User user) {
        return
                """
                        Dzień dobry %s,
                        
                        skorzystałeś z przypomnienia hasła.
                        
                        Poniżej znajdziesz link służący do ustawienia nowego hasła:
                        %s
                        
                        Proszę o zignorowanie wiadomości jeżeli to nie Ty korzystałeś z przypomnienia hasła.
                        
                        Pozdrawiamy,
                        
                        """.formatted(user.getNick(), generateRemindPassUrl(user));
    }

    private String generateRemindPassUrl(User user) {
        return String.format("%s/ustaw-nowe-haslo/%d?activKey=%s", url, user.getId(), user.getActivKey());
    }

    String generateContactEmail(String name, String email, String message) {
        return """
                Wiadomość od użytkownika o imieniu: %s.
                Podany adres email: %s
                
                Treść wiadomości:
                %s
                
                """.formatted(name, email, message);
    }

    String generateCommentNotificationEmail(User user, Fish fish) {
        return """
                Dzień dobry %s,
                
                ktoś dodał komentarz do Twojej ryby! Sprawdź to!
                
                %s
                
                Pozdrawiamy,
                """.formatted(user.getNick(), generateUrlToFish(fish));
    }

    String generateRatingNotificationEmail(User user, Fish fish) {
        return """
                Dzień dobry %s,
                
                ktoś ocenił Twoją rybę! Sprawdź to!
                
                %s
                
                Pozdrawiamy,
                """.formatted(user.getNick(), generateUrlToFish(fish));
    }

    private String generateUrlToFish(Fish fish) {
        return String.format("%s/okaz/%d", url, fish.getId());
    }
}
