package matt.pass.mojaryba.domain.user.exceptions;

public class NickExistsException extends RuntimeException{
    public NickExistsException(String nick) {
        super("Podany nick jest zajęty: " + nick);
    }
}
