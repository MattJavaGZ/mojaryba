package matt.pass.mojaryba.web;

import matt.pass.mojaryba.domain.fish.FishService;
import matt.pass.mojaryba.domain.fish.dto.FishDto;
import matt.pass.mojaryba.domain.type.FishTypeService;
import matt.pass.mojaryba.domain.type.dto.FishTypeDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FishTypeController {
    private final FishTypeService fishTypeService;
    private final FishService fishService;

    public FishTypeController(FishTypeService fishTypeService, FishService fishService) {
        this.fishTypeService = fishTypeService;
        this.fishService = fishService;
    }

    @GetMapping("/gatunek/{fishTypeName}")
    String findById(Model model, @PathVariable String fishTypeName, @RequestParam(required = false, defaultValue = "0") int page) {
        final List<FishDto> fishesByType = fishService.findFishesByType(fishTypeName, page);
        int totalPages = fishService.totalPagesFishesByType(fishTypeName, page);
        model.addAttribute("heading", "Gatunek: " + fishTypeName);
        model.addAttribute("fishes", fishesByType);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("url", "/gatunek/" + fishTypeName);
        return "fish-listing";
    }
    @GetMapping("/gatunki")
    String allFishTypes(Model model){
        final List<FishTypeDto> allTypes = fishTypeService.findAllTypes();
        model.addAttribute("types", allTypes);
        return "types-listing";
    }
}
