package EVC.Movie.validator;

import EVC.Movie.entity.User;
import EVC.Movie.repository.IUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail,String> {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (userRepository == null) {
                return true;
            }
            User user = userRepository.findFirstByEmail(email);
            return user == null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
