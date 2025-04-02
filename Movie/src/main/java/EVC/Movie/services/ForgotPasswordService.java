package EVC.Movie.services;

import EVC.Movie.entity.ForgotPassword;
import EVC.Movie.entity.User;
import EVC.Movie.repository.IForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForgotPasswordService {
    @Autowired
    private IForgotPasswordRepository forgotPasswordRepository;
    public void save(ForgotPassword forgotPassword){
        forgotPasswordRepository.save(forgotPassword);
    }

    public ForgotPassword findInfoByOTPAndUser(Integer otp, User user){
        return forgotPasswordRepository.findByOtpAndUser(otp, user);
    }

    public ForgotPassword findInfoByUser(User user){
        return forgotPasswordRepository.findByUser(user);
    }
    public void delete(ForgotPassword forgotPassword){
        forgotPasswordRepository.deletePass(forgotPassword.getId());
    }
}
