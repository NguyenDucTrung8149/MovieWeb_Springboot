package EVC.Movie.utils;

import EVC.Movie.entity.ForgotPassword;
import EVC.Movie.entity.User;
import EVC.Movie.services.ForgotPasswordService;
import EVC.Movie.services.UserServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserServices userServices;
    private final ForgotPasswordService forgotPasswordService;

    public CustomAuthenticationSuccessHandler(UserServices userServices, ForgotPasswordService forgotPasswordService) {
        this.userServices = userServices;
        this.forgotPasswordService = forgotPasswordService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            User user = userServices.findByUsername(authentication.getName());
            ForgotPassword existingFp = forgotPasswordService.findInfoByUser(user);
            if (existingFp != null) {
                forgotPasswordService.delete(existingFp);
            }
        }
        response.sendRedirect("/");
    }

}
