package matt.pass.mojaryba.web;

import matt.pass.mojaryba.domain.fish.FishService;
import matt.pass.mojaryba.domain.fish.dto.FishDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    private final FishService fishService;

    public HomeController(FishService fishService) {
        this.fishService = fishService;
    }

    @GetMapping("/")
    String home(Model model, @RequestParam(required = false, defaultValue = "0") int page){
        int totalPages = fishService.totalPagesAllFishes(page);
        List<FishDto> allFishes = fishService.findAllFishes(page);
        model.addAttribute("heading", "Najnowsze okazy");
        model.addAttribute("description", "Tutaj znajdziesz najnowsze okazy naszych użytkowników");
        model.addAttribute("fishes", allFishes);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("url", "/");
        return "fish-listing";

    }
}
