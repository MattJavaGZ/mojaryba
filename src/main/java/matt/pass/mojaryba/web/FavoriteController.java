package matt.pass.mojaryba.web;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import matt.pass.mojaryba.domain.favorite.FavoriteService;
import matt.pass.mojaryba.domain.fish.dto.FishDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Controller
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favorite")
    String favorite(Model model, @CookieValue(value = "favorite", defaultValue = "") String favorite) {
        final Set<FishDto> favoritesList = favoriteService.getFavoritesList(favorite);
        model.addAttribute("heading", "Ulubione");
        model.addAttribute("description", "W tej sekcji znajdziesz okazy dodane przez Ciebie do ulubionych");
        model.addAttribute("fishes", favoritesList);
        return "fish-listing";
    }
    @GetMapping("/favorite/add/{id}")
    String addToFavorite(@PathVariable long id, @CookieValue(value = "favorite", defaultValue = "") String favorite,
                         HttpServletResponse response, @RequestHeader String referer) {
        final String updatedFavoroteCookie = favoriteService.addToFavorite(favorite, id);
        createAndSendFavoriteCookie(response, updatedFavoroteCookie);
        return "redirect:" + referer;
    }
    @GetMapping("/favorite/delete/{id}")
    String deleteFromFavorite(@PathVariable long id,@CookieValue(value = "favorite", defaultValue = "") String favorite,
                              HttpServletResponse response,  @RequestHeader String referer) {
        final String updatedFavoroteCookie = favoriteService.deleteWithFavorite(favorite, id);
        createAndSendFavoriteCookie(response, updatedFavoroteCookie);
        return "redirect:" + referer;
    }
    private void createAndSendFavoriteCookie(HttpServletResponse response, String favorite) {
        final Cookie cookie = new Cookie("favorite", URLEncoder.encode(favorite, StandardCharsets.UTF_8));
        cookie.setMaxAge(365 * 24 * 60 * 60);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

}
