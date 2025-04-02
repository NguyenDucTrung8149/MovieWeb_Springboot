package EVC.Movie.services;

import EVC.Movie.entity.CustomUserDetail;
import EVC.Movie.entity.User;
import EVC.Movie.repository.IUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserServices userServices;
    @Autowired
    private IUserRepository userRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Lấy thông tin người dùng từ CustomOAuth2UserService
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Lấy thông tin người dùng từ cơ sở dữ liệu
        String email = (String) attributes.get("email");
        User user = userServices.findByUsername(email);

        // Tạo đối tượng Authentication
        CustomUserDetail customUserDetail = new CustomUserDetail(user, userRepository);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(customUserDetail, null, customUserDetail.getAuthorities());

        // Thiết lập SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authToken);

        if (user.isLocked() ==  true){
            response.sendRedirect("/user/login");
        }else {
            // Chuyển hướng sau khi đăng nhập thành công
            response.sendRedirect("/");
        }

    }
}