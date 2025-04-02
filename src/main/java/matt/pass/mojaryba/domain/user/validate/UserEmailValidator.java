package matt.pass.mojaryba.domain.user.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import matt.pass.mojaryba.domain.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserEmailValidator implements ConstraintValidator<UserEmailConstraint, String> {

    private final UserService userService;

    public UserEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UserEmailConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !userService.chechExistByEmail(email);
    }
}
