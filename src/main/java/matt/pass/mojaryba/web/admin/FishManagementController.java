package matt.pass.mojaryba.web.admin;

import jakarta.validation.Valid;
import matt.pass.mojaryba.domain.fish.FishService;
import matt.pass.mojaryba.domain.fish.dto.FishToSaveDto;
import matt.pass.mojaryba.domain.type.FishTypeService;
import matt.pass.mojaryba.domain.type.dto.FishTypeDto;
import matt.pass.mojaryba.domain.user.User;
import matt.pass.mojaryba.domain.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FishManagementController {
    public final static String NOTIFICATION_ATTRIBUTE = "notification";
    private final FishService fishService;
    private final FishTypeService fishTypeService;
    private final UserService userService;

    public FishManagementController(FishService fishService, FishTypeService fishTypeService, UserService userService) {
        this.fishService = fishService;
        this.fishTypeService = fishTypeService;
        this.userService = userService;
    }

    @GetMapping("/dodaj-okaz")
    String addFishForm(Model model) {
        final FishToSaveDto fish = new FishToSaveDto();
        model.addAttribute("fish", fish);
        getAllTypes(model);
        return "fish-add-form";
    }

    @PostMapping("/dodaj-okaz")
    String addFish(Model model, @Valid @ModelAttribute("fish") FishToSaveDto fish, BindingResult bindingResult,
                   RedirectAttributes redirectAttributes, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            getAllTypes(model);
            return "fish-add-form";
        } else {
            final String userEmail = authentication.getName();
            final User user = userService.findUserByEmail(userEmail).orElseThrow();
            final long savedFishId = fishService.createFishFromForm(fish, user);
            redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE, "Okaz został dodany");
            return "redirect:/okaz/" + savedFishId;
        }
    }

    private void getAllTypes(Model model) {
        List<FishTypeDto> types = fishTypeService.findAllTypes();
        model.addAttribute("types", types);
    }

    @GetMapping("/admin/usun/{id}")
    String deleteFish(@PathVariable long id) {
        fishService.deleteFishById(id);
        return "redirect:/";
    }

    @GetMapping("/okaz/edytuj/{id}")
    String editFishForm(Model model, @PathVariable long id) {
        final boolean isAdminOrAuthor = fishService.verificationFishAuthorOrAdmin(id);
        if (!isAdminOrAuthor) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else {
            final FishToSaveDto fishToEdit = fishService.findByIdToSave(id);
            model.addAttribute("fish", fishToEdit);
            model.addAttribute("id", id);
            getAllTypes(model);
            return "fish-edit-form";
        }

    }

    @PostMapping("/okaz/edytuj/{id}")
    String editFish(Model model, @PathVariable long id, @Valid @ModelAttribute("fish") FishToSaveDto fish,
                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        final boolean isAdminOrAuthor = fishService.verificationFishAuthorOrAdmin(id);
        if (!isAdminOrAuthor) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else {
            if (bindingResult.hasErrors()) {
                getAllTypes(model);
                return "fish-edit-form";
            } else {
                fishService.editFish(fish, id);
                redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE, "Okaz został zaktualizowany");
                return "redirect:/okaz/" + id;
            }
        }
    }


}
