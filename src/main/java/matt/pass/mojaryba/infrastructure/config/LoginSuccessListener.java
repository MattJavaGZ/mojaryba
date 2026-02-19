package matt.pass.mojaryba.infrastructure.config;

import matt.pass.mojaryba.domain.user.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessListener {

    private final UserService userService;

    public LoginSuccessListener(UserService userService) {
        this.userService = userService;
    }

    @EventListener
    public void onLoginSuccess(AuthenticationSuccessEvent event) {
        final Object principal = event.getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            final String username = userDetails.getUsername();
            userService.updateLastLoginDate(username);
        }
    }
}
