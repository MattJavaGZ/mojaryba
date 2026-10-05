package matt.pass.mojaryba.web.admin;

import groovy.util.logging.Log4j2;
import matt.pass.mojaryba.domain.user.UserService;
import matt.pass.mojaryba.infrastructure.email.EmailService;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
public class PasswordRemindController {
    private static final Logger log = LoggerFactory.getLogger(PasswordRemindController.class);
    private final UserService userService;
    private final EmailService emailService;

    public PasswordRemindController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/przypomnienie-hasla")
    String passRemindForm() {
        return "remind-pass-form";
    }

    @PostMapping("/przypomnienie-hasla")
    String remindPass(@RequestParam String email, RedirectAttributes redirectAttributes) {

        String message = "Jeżeli adres email %s jest poprawny, otrzymałeś link służący do ustawienia nowego hasła. Sprawdź spam"
                .formatted(email);

        userService.findUserByEmail(email).ifPresentOrElse(
                user -> {
                    try {
                        userService.generateNewRemindPassKey(user);
                        emailService.sendRemindPassEmail(user);
                        redirectAttributes.addFlashAttribute(
                                FishManagementController.NOTIFICATION_ATTRIBUTE, message);
                    } catch (EmailException e) {
                        log.error("Błąd podczas wysyłania wiadomości z przypomnienie hasła na email {}", user.getEmail());
                        redirectAttributes.addFlashAttribute(
                                FishManagementController.NOTIFICATION_ATTRIBUTE,
                                "Błąd podczas wysyłania wiadomości email. Spróuj ponownie");
                    }
                },
                () -> redirectAttributes.addFlashAttribute(
                        FishManagementController.NOTIFICATION_ATTRIBUTE, message)
        );
        return "redirect:/przypomnienie-hasla";
    }

    @GetMapping("/ustaw-nowe-haslo")
    String setNewPassForm(Model model, @RequestParam String remindPassKey) {
        model.addAttribute("remindPassKey", remindPassKey);
        return "set-pass-form";
    }

    @PostMapping("/ustaw-nowe-haslo")
    String setNewPass(@RequestParam String remindPassKey, @RequestParam String password,
                      RedirectAttributes redirectAttributes) {

        if (userService.setNewPass(remindPassKey, password)) {
            redirectAttributes.addFlashAttribute(FishManagementController.NOTIFICATION_ATTRIBUTE,
                    "Hasło zostało pomyślnie zmienione");
        } else {
            redirectAttributes.addFlashAttribute(FishManagementController.NOTIFICATION_ATTRIBUTE,
                    "Błąd podczas zmiany hasła. Link wygasł albo użyłeś błędnego linku. Spróbuj ponownie");
        }
        return "redirect:/login";
    }

}
