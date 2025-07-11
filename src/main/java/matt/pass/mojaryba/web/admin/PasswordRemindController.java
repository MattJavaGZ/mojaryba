package matt.pass.mojaryba.web.admin;

import matt.pass.mojaryba.domain.user.UserService;
import matt.pass.mojaryba.infrastructure.email.EmailService;
import org.apache.commons.mail.EmailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordRemindController {
    private UserService userService;
    private EmailService emailService;

    public PasswordRemindController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/przypomnienie-hasla")
    String passRemindForm() {
        return "remind-pass-form";
    }

    @PostMapping("/przypomnienie-hasla")
        String remindPass(@RequestParam String email, RedirectAttributes redirectAttributes){
        userService.findUserByEmail(email).ifPresentOrElse(
                user -> {
                    try {
                        emailService.sendRemindPassEmail(user);
                        redirectAttributes.addFlashAttribute(
                                FishManagementController.NOTIFICATION_ATTRIBUTE,
                                "Na adres email %s został wysłany link służący do ustawienia nowego hasła.".formatted(email));
                    } catch (EmailException e) {
                        e.printStackTrace();
                        redirectAttributes.addFlashAttribute(
                                FishManagementController.NOTIFICATION_ATTRIBUTE,
                                "Błąd podczas wysyłania wiadomości email. Spróuj ponownie");
                    }
                },
                () ->  redirectAttributes.addFlashAttribute(
                        FishManagementController.NOTIFICATION_ATTRIBUTE,
                        "Błędy adres email. Spróbuj ponownie")
        );
        return "redirect:/przypomnienie-hasla";
    }

    @GetMapping("/ustaw-nowe-haslo/{id}")
    String setNewPassForm(Model model,@PathVariable long id, @RequestParam String activKey){
        model.addAttribute("id", id);
        model.addAttribute("activKey", activKey);
        return "set-pass-form";
    }
    @PostMapping("/ustaw-nowe-haslo/{id}")
    String setNewPass(@PathVariable long id, @RequestParam String activKey, @RequestParam String password,
                      RedirectAttributes redirectAttributes){
        if (userService.setNewPass(id, activKey, password)) {
            redirectAttributes.addFlashAttribute(FishManagementController.NOTIFICATION_ATTRIBUTE,
                    "Hasło zostało pomyślnie zmienione");
        } else {
            redirectAttributes.addFlashAttribute(FishManagementController.NOTIFICATION_ATTRIBUTE,
                    "Błąd poczas zmiany hasła. Użyj linku z maila nie zmieniając jego zawartości");
        }
        return "redirect:/login";
    }

}
