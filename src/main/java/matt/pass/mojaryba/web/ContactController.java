package matt.pass.mojaryba.web;

import groovy.util.logging.Log4j2;
import matt.pass.mojaryba.infrastructure.email.EmailService;
import matt.pass.mojaryba.web.admin.FishManagementController;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
public class ContactController {
    private static final Logger log = LoggerFactory.getLogger(ContactController.class);
    private final EmailService emailService;

    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/kontakt")
        String contactForm() {
        return "contact";
    }
    @PostMapping("/kontakt")
        String contactMessage(@RequestParam(required = false, defaultValue = "Brak") String name, @RequestParam String email,
                              @RequestParam String message, RedirectAttributes redirectAttributes) {
        try {
            emailService.sendContactEmail(name, email, message);
            redirectAttributes.addFlashAttribute(FishManagementController.NOTIFICATION_ATTRIBUTE,
                    "Wiadomość została wysłana");
            return "redirect:/kontakt";
        } catch (EmailException e) {
            redirectAttributes.addFlashAttribute(FishManagementController.NOTIFICATION_ATTRIBUTE,
                    "Błąd podczas wysyłania wiadomości. Spróbuj ponownie");
            log.error("Błąd podczas wysyłania wiadomości kontaktowej z adresu email {}", email);
            return "redirect:/kontakt";
        }

    }
}
