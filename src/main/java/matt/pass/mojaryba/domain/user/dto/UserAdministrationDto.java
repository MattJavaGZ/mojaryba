package matt.pass.mojaryba.domain.user.dto;

import matt.pass.mojaryba.infrastructure.config.CustomSecurityService;

import java.util.List;

public class UserAdministrationDto {

    private Long id;
    private String email;
    private String password;
    private String nick;
    private boolean activ;
    private List<String> roles;

    public UserAdministrationDto(Long id, String email, String password, String nick, boolean activ, List<String> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nick = nick;
        this.activ = activ;
        this.roles = roles;
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
}
