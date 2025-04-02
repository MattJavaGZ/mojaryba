package matt.pass.mojaryba.web.admin;

import jakarta.validation.Valid;
import matt.pass.mojaryba.domain.user.UserAdminService;
import matt.pass.mojaryba.domain.user.UserService;
import matt.pass.mojaryba.domain.user.dto.UserRegisterDto;
import org.apache.commons.mail.EmailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {
    private final UserService userService;
    private final UserAdminService userAdminService;

    public RegisterController(UserService userService, UserAdminService userAdminService) {
        this.userService = userService;
        this.userAdminService = userAdminService;
    }

    @GetMapping("/rejestracja")
    String registerForm(Model model) {
        final UserRegisterDto user = new UserRegisterDto();
        model.addAttribute("user", user);
        return "register-form";
    }

    @PostMapping("/rejestracja")
    String register(Model model, @Valid @ModelAttribute(name = "user") UserRegisterDto user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register-form";
        } else {
            try {
                userService.register(user);
                model.addAttribute("heading", "Rejestracja przebiegła pomyślnie");
                model.addAttribute("description",
                        "Na Twój adres email została wysłana wiadomość z linkiem aktywacyjnym. Aktywuj konto");
                return "activation-page";
            } catch (EmailException e) {
                System.err.println("Problem z wysyłką email");
                e.printStackTrace();
                userAdminService.deleteUserByEmail(user.getEmail());
                model.addAttribute("heading", "Błąd podczas rejestracji");
                model.addAttribute("description",
                        "Nie udało się wysłać wiadomości z linkiem aktywacyjny. Dokonaj rejestracji ponownie");
                return "register-form";
            }

        }
    }

    @GetMapping("/aktywacja/{id}")
    String activation(Model model, @PathVariable long id, @RequestParam String activKey, RedirectAttributes redirectAttributes) {
        final boolean activationSuccess = userService.checkAndActivUserAccount(id, activKey);
        if (activationSuccess) {
            redirectAttributes.addFlashAttribute(
                    FishManagementController.NOTIFICATION_ATTRIBUTE, "Twoje konto zostało pomyślnie aktywowane. Zaloguj się");
            return "redirect:/login";
        } else {
            model.addAttribute("heading", "Wystąpił problem z aktywacją konta");
            model.addAttribute("description",
                    "Aktywuj konto ponownie poprzez link wysłany na email nie zmieniając jego zawartości");
            return "activation-page";
        }
    }
}

