package matt.pass.mojaryba.web;

import matt.pass.mojaryba.domain.fish.FishService;
import matt.pass.mojaryba.domain.fish.dto.FishDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller

public class UserPageController {
     private final FishService fishService;

    public UserPageController(FishService fishService) {
        this.fishService = fishService;
    }

    @GetMapping("/uzytkownik/{nick}")
    String userPage (Model model, @PathVariable String nick) {
        final List<FishDto> allByUserNick = fishService.findAllByUserNick(nick);
        model.addAttribute("fishes", allByUserNick);
        model.addAttribute("heading", String.format("Okazy użytkownika %s", nick));
        if (allByUserNick.isEmpty()) model.addAttribute("description", "Brak wstawionych okazów");
        return "fish-listing";
    }
}
