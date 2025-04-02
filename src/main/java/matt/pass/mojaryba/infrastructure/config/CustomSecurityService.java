package matt.pass.mojaryba.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class CustomSecurityService {

    public final static String USER_ROLE = "USER";
    public final static String ADMIN_ROLE = "ADMIN";
    public final static String BLOCKED_ROLE = "BLOCKED";


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/admin/**").hasRole(ADMIN_ROLE)
                .requestMatchers("/dodaj-okaz/**").hasAnyRole(ADMIN_ROLE, USER_ROLE)
                .requestMatchers("/like/**").hasAnyRole(ADMIN_ROLE, USER_ROLE)
                .requestMatchers("/dodaj-komentarz").hasAnyRole(ADMIN_ROLE, USER_ROLE)
                .requestMatchers("/ocen-rybe").hasAnyRole(ADMIN_ROLE, USER_ROLE)
                .requestMatchers("/okaz/edytuj/**").hasAnyRole(ADMIN_ROLE, USER_ROLE)
                .requestMatchers("/panel/**").authenticated()
                .anyRequest().permitAll()
        );
        http.formLogin(login -> login.loginPage("/login").permitAll());
        http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout/**", HttpMethod.GET.name()))
                .logoutSuccessUrl("/")
        );
//        http.csrf(csrt -> csrt.ignoringRequestMatchers(PathRequest.toH2Console()));
        http.csrf().ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"));
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/img/**",
                "/scripts/**",
                "/styles/**"
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
