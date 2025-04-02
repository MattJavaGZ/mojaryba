package matt.pass.mojaryba.web;

import matt.pass.mojaryba.domain.comment.CommentService;
import matt.pass.mojaryba.domain.comment.dto.CommentDto;
import matt.pass.mojaryba.domain.favorite.FavoriteService;
import matt.pass.mojaryba.domain.fish.FishSearchService;
import matt.pass.mojaryba.domain.fish.FishService;
import matt.pass.mojaryba.domain.fish.FishTopService;
import matt.pass.mojaryba.domain.fish.dto.FishDto;
import matt.pass.mojaryba.domain.rating.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Controller
public class FishController {
     private final FishService fishService;
     private final FishTopService fishTopService;
     private final RatingService ratingService;
     private final CommentService commentService;
     private final FavoriteService favoriteService;

    public FishController(FishService fishService, FishTopService fishTopService, RatingService ratingService,
                          CommentService commentService, FavoriteService favoriteService) {
        this.fishService = fishService;
        this.fishTopService = fishTopService;
        this.ratingService = ratingService;
        this.commentService = commentService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/okaz/{id}")
    String singleFishPage(Model model, @PathVariable long id, @RequestParam(required = false) String commentsSize,
                          Authentication authentication, @CookieValue(value = "favorite", defaultValue = "") String favorite) {

        final FishDto fishDto = fishService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("fish", fishDto);

        final Set<FishDto> favorites = favoriteService.getFavoritesList(favorite);
        model.addAttribute("favorites", favorites);

        if (authentication != null) {
            final String userEmail = authentication.getName();
            final boolean isAdminOrAuthor = fishService.verificationFishAuthorOrAdmin(id);
            final Integer currentRating = ratingService.getCurrentRating(userEmail, id);
            model.addAttribute("currentRating", currentRating);
            model.addAttribute("edit", isAdminOrAuthor);

        }

        List<CommentDto> commentsForFish = commentService.getCommentsForFish(id);
        if (commentsSize == null) {
            model.addAttribute("comments", commentsForFish.stream()
                    .limit(5)
                    .toList());
        } else {
            model.addAttribute("comments", commentsForFish);
        }
        return "fish";
    }

    @GetMapping("/top10/oceniane")
    String top10Rated(Model model) {
        final List<FishDto> top10Fishes = fishTopService.getTop10RatedFishes();
        model.addAttribute("heading", "Top10");
        model.addAttribute("description", "W tej sekcji znajdziesz najlepiej ocenione okazy");
        model.addAttribute("fishes", top10Fishes);
        return "top10";
    }

    @GetMapping("/top10/lubiane")
    String top10Liked(Model model) {
        final List<FishDto> top10LikedFishes = fishTopService.getTop10LikedFishes();
        model.addAttribute("heading", "Top10");
        model.addAttribute("description", "W tej sekcji znajdziesz najbardziej lubiane okazy");
        model.addAttribute("fishes", top10LikedFishes);
        return "top10";
    }

    @GetMapping("/top10/najwieksze")
    String top10Bigest(Model model) {
        final List<FishDto> top10BigestFishes = fishTopService.getTop10BigestFishes();
        model.addAttribute("heading", "Top10");
        model.addAttribute("description", "W tej sekcji znajdziesz największe/najcięższe ryby");
        model.addAttribute("fishes", top10BigestFishes);
        return "top10";
    }

}
