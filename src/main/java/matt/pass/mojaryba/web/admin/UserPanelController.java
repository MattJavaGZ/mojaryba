package matt.pass.mojaryba.web.admin;

import matt.pass.mojaryba.domain.fish.FishSearchService;
import matt.pass.mojaryba.domain.fish.FishService;
import matt.pass.mojaryba.domain.fish.dto.FishDto;
import matt.pass.mojaryba.domain.type.FishTypeService;
import matt.pass.mojaryba.domain.type.dto.FishTypeDto;
import matt.pass.mojaryba.domain.user.UserAdminService;
import matt.pass.mojaryba.domain.user.UserService;
import matt.pass.mojaryba.domain.user.dto.UserAdministrationDto;
import matt.pass.mojaryba.domain.user.exceptions.NickExistsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class UserPanelController {
    private final FishService fishService;
    private final FishSearchService fishSearchService;
    private final FishTypeService fishTypeService;
    private final UserService userService;
    private final UserAdminService userAdminService;

    public UserPanelController(FishService fishService, FishSearchService fishSearchService,
                               FishTypeService fishTypeService, UserService userService, UserAdminService userAdminService) {
        this.fishService = fishService;
        this.fishSearchService = fishSearchService;
        this.fishTypeService = fishTypeService;
        this.userService = userService;
        this.userAdminService = userAdminService;
    }

    @GetMapping("/panel")
    String userPanel(Authentication authentication, RedirectAttributes redirectAttributes) {
        final String userEmail = authentication.getName();
        if (userService.isBlocked(userEmail)) {
            redirectAttributes.addFlashAttribute(FishManagementController.NOTIFICATION_ATTRIBUTE,
                    "Twoje konto zostało zablokowane");
        }
        return "redirect:/panel/moje-okazy";
    }

    @GetMapping("/panel/moje-okazy")
    String userPanelMyFishes(Model model, Authentication authentication) {
        final String userEmail = authentication.getName();
        final List<FishDto> fishesByUserEmail = fishService.findAllByUserEmail(userEmail);
        model.addAttribute("heading", "Moje okazy");
        model.addAttribute("fishes", fishesByUserEmail);
        return "user-panel";
    }

    @GetMapping("/panel/skomentowane")
    String userPanelCommentedFishes(Model model, Authentication authentication) {
        final String userEmail = authentication.getName();
        final List<FishDto> allCommentedFishesByUser = fishService.findAllCommentedFishesByUser(userEmail);
        model.addAttribute("heading", "Skomentowane okazy");
        model.addAttribute("fishes", allCommentedFishesByUser);
        return "user-panel";
    }

    @GetMapping("/panel/ocenione")
    String userPanelRatedFishes(Model model, Authentication authentication) {
        final String userEmail = authentication.getName();
        final List<FishDto> allRatedFishesByUser = fishService.findAllRatedFishesByUser(userEmail);
        model.addAttribute("heading", "Ocenione okazy");
        model.addAttribute("fishes", allRatedFishesByUser);
        return "user-panel";
    }

    @GetMapping("/panel/polubione")
    String userPanelLikedFishes(Model model, Authentication authentication) {
        final String userEmail = authentication.getName();
        final List<FishDto> allLikedFishesByUser = fishService.findAllLikedFishesByUser(userEmail);
        model.addAttribute("heading", "Polubione okazy");
        model.addAttribute("fishes", allLikedFishesByUser);
        return "user-panel";
    }

    @GetMapping("/panel/dziennik-polowow")
    String userPanelFishingLog(Model model) {
        sendAllTypes(model);
        model.addAttribute("heading", "dziennik polowow");
        return "user-panel-fishing-log";
    }

    private void sendAllTypes(Model model) {
        final List<FishTypeDto> allTypes = fishTypeService.findAllTypes();
        model.addAttribute("types", allTypes);
    }

    @GetMapping("/panel/dziennik-polowow/gatunek")
    String fishingLogByType(Model model, @RequestParam String type, Authentication authentication) {
        final String userEmail = authentication.getName();
        final List<FishDto> fishesByTypeAndUser = fishService.findFishesByTypeAndUser(type, userEmail);
        String summary = String.format("Gatunek %s - złowiłeś %d szt.", type, fishesByTypeAndUser.size());
        sendAtributes(model, fishesByTypeAndUser, summary);
        model.addAttribute("heading", "dziennik polowow");
        return "user-panel-fishing-log";
    }

    @GetMapping("/panel/dziennik-polowow/szukaj")
    String fishingLogFind(Model model, @RequestParam String find, Authentication authentication) {
        final String userEmail = authentication.getName();
        final List<FishDto> fishes = fishSearchService.searchInUserFishes(userEmail, find);
        String summary = String.format("Wyszukane okazy - %d szt.", fishes.size());
        sendAtributes(model, fishes, summary);
        model.addAttribute("heading", "dziennik polowow");
        return "user-panel-fishing-log";
    }

    private void sendAtributes(Model model, List<FishDto> fishes, String summary) {
        model.addAttribute("summary", summary);
        model.addAttribute("fishes", fishes);
        sendAllTypes(model);
    }

    @GetMapping("/panel/dziennik-polowow/data")
    String fishingLogDate(Model model, @RequestParam String start, @RequestParam String end,
                          Authentication authentication) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate startDate = LocalDate.parse(start, formatter);
        final LocalDate endDate = LocalDate.parse(end, formatter);
        final String userEmail = authentication.getName();
        final List<FishDto> fishesByUserAndDate = fishService.findFishesByUserAndDate(userEmail, startDate, endDate);
        String summary = String.format("W okresie od %s do %s złowiłeś %d szt.",
                start, end, fishesByUserAndDate.size());
        sendAtributes(model, fishesByUserAndDate, summary);
        model.addAttribute("heading", "dziennik polowow");
        return "user-panel-fishing-log";
    }

    @GetMapping("/panel/edycja-konta")
    String userPanelEdit(Model model, Authentication authentication) {
        final String userEmail = authentication.getName();
        final UserAdministrationDto user = userService.findUserAdministrationByEmail(userEmail).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("heading", "edycja konta");
        return "user-panel-edit";
    }

    @PostMapping("/panel/edycja-konta/edytuj-nick")
    String userPanelEditNick(@RequestParam String nick, Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        final String userEmail = authentication.getName();
        try {
            userAdminService.adminEditUserNickByEmail(nick, userEmail);
            redirectAttributes.addFlashAttribute(FishManagementController.NOTIFICATION_ATTRIBUTE,
                    "Nick został zmieniony");
        } catch (NickExistsException e) {
            redirectAttributes.addFlashAttribute(FishManagementController.NOTIFICATION_ATTRIBUTE,
                    e.getMessage());
        }

        return "redirect:/panel/edycja-konta";
    }

    @PostMapping("/panel/edycja-konta/edytuj-haslo")
    String userPanelEditPassword(@RequestParam String password1, @RequestParam String password2,
                                 Authentication authentication, RedirectAttributes redirectAttributes) {
        final String userEmail = authentication.getName();

        if (password1.equals(password2)) {
            userAdminService.adminEditUserPassByEmail(password1, userEmail);
            redirectAttributes.addFlashAttribute(FishManagementController.NOTIFICATION_ATTRIBUTE,
                    "Hasło zostało zmienione");
        } else {
            redirectAttributes.addFlashAttribute(FishManagementController.NOTIFICATION_ATTRIBUTE,
                    "Hasła różnią się od siebie. Zmień ponownie");
        }

        return "redirect:/panel/edycja-konta";
    }
}

