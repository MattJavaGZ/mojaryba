package matt.pass.mojaryba.web.admin;


import matt.pass.mojaryba.domain.email.EmailSettingsService;
import matt.pass.mojaryba.domain.email.dto.EmailSettingsWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminEmailSettingController {

    private final EmailSettingsService emailSettingsService;

    public AdminEmailSettingController(EmailSettingsService emailSettingsService) {
        this.emailSettingsService = emailSettingsService;
    }

    @GetMapping("/admin/mailing")
    public String emailSettingForm(Model model) {
        final EmailSettingsWrapper settings = emailSettingsService.findAllEmailSettingsFormWrap();
        model.addAttribute("heading", "mailing");
        model.addAttribute("settings", settings);
        return "admin-panel-mailing";
    }

    @PostMapping("/admin/mailing")
    public String saveEmailSetting(@ModelAttribute("settings") EmailSettingsWrapper settings) {
        emailSettingsService.saveEmailSettings(settings);
        return "redirect:/admin/mailing";
    }
}
