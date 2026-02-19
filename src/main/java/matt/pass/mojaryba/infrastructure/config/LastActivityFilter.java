package matt.pass.mojaryba.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import matt.pass.mojaryba.domain.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class LastActivityFilter extends OncePerRequestFilter {

    private static final Duration UPDATE_INTERVAL = Duration.ofMinutes(5);

    private final UserService userService;

    public LastActivityFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null
                && auth.isAuthenticated()
                && auth.getPrincipal() instanceof UserDetails user
                && isPageRequest(request)) {

            final String username = user.getUsername();

            if (shouldUpdate(request)){
                userService.updateLastActivDate(username);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPageRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return !uri.startsWith("/static/")
                && !uri.endsWith(".css")
                && !uri.endsWith(".js")
                && !uri.endsWith(".png")
                && !uri.endsWith(".jpg")
                && !uri.contains("/api/");
    }

    private boolean shouldUpdate(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final LocalDateTime lastActiv = (LocalDateTime) session.getAttribute("LAST_ACTIVITY_UPDATE");

        final LocalDateTime now = LocalDateTime.now();

        if (lastActiv == null || lastActiv.plus(UPDATE_INTERVAL).isBefore(now)) {
            session.setAttribute("LAST_ACTIVITY_UPDATE", LocalDateTime.now());
            return true;
        }
        return false;
    }
}
