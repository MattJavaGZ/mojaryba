package matt.pass.mojaryba.domain.user.dto;

import matt.pass.mojaryba.infrastructure.config.CustomSecurityService;

import java.time.LocalDateTime;
import java.util.List;

public class UserAdministrationDto {

    private Long id;
    private String email;
    private String password;
    private String nick;
    private boolean activ;
    private List<String> roles;
    private LocalDateTime lastLoginDate;
    private LocalDateTime lastActivDate;

    public UserAdministrationDto(Long id, String email, String password, String nick, boolean activ, List<String> roles,
                                 LocalDateTime lastLoginDate, LocalDateTime lastActivDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nick = nick;
        this.activ = activ;
        this.roles = roles;
        this.lastLoginDate = lastLoginDate;
        this.lastActivDate = lastActivDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public boolean isActiv() {
        return activ;
    }

    public void setActiv(boolean activ) {
        this.activ = activ;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isBlocked() {
        return roles.contains(CustomSecurityService.BLOCKED_ROLE);
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public LocalDateTime getLastActivDate() {
        return lastActivDate;
    }

    public void setLastActivDate(LocalDateTime lastActivDate) {
        this.lastActivDate = lastActivDate;
    }
}
