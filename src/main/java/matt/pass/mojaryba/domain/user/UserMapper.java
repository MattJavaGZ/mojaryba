package matt.pass.mojaryba.domain.user;

import matt.pass.mojaryba.domain.user.dto.UserAdministrationDto;
import matt.pass.mojaryba.domain.user.dto.UserCredentialsDto;

public class UserMapper {

    public static UserCredentialsDto map (User user) {
        return new UserCredentialsDto(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(UserRole::getName)
                        .toList()
        );
    }
    public static UserAdministrationDto mapToUserAdministration(User user) {
        return new UserAdministrationDto(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getNick(),
                user.isActiv(),
                user.getRoles().stream().map(UserRole::getName).toList(),
                user.getLastLoginDate(),
                user.getLastActivDate()
        );
    }
}
