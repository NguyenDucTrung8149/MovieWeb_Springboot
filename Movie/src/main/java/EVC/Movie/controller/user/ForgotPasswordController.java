package EVC.Movie.controller.user;

import EVC.Movie.entity.ForgotPassword;
import EVC.Movie.entity.User;
import EVC.Movie.services.EmailService;
import EVC.Movie.services.ForgotPasswordService;
import EVC.Movie.services.UserServices;
import EVC.Movie.validator.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping("/user/forgotPassword")
public class ForgotPasswordController {
    @Autowired
    private UserServices userServices;

    @Autowired
    private EmailService emailService;
    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/verifyEmail")
    public String verifyEmailForm() {
        return "/user/forgotPassword/verifyEmail";
    }

    @PostMapping("/verifyEmail")
    public String verifyEmail(@RequestParam String email, Model model) {
        User user = userServices.findFirstbyEmail(email);
        if(user == null) {
            model.addAttribute("InvalidEmail", "Email không tồn tại");
            return "/user/forgotPassword/verifyEmail";
        }
        int otp = otpGenerator();
        ForgotPassword existingFp = forgotPasswordService.findInfoByUser(user);
        if(existingFp != null) {
            forgotPasswordService.delete(existingFp);
        }
        ForgotPassword fp = new ForgotPassword();
        fp.setUser(user);
        fp.setOtp(otp);
        fp.setExpiredTime(new Date(System.currentTimeMillis() + 60 * 1000));

        String subject = "OTP quên mật khẩu web xem phim";
        String text = "Đây là OTP để reset mật khẩu: " + otp;

        emailService.sendSimpleMessage(email,subject,text);
        forgotPasswordService.save(fp);
        model.addAttribute("email", email);
        return "redirect:/user/forgotPassword/verifyOtp/" + user.getId();
    }

    @GetMapping("/verifyOtp/{id}")
    public String verifyOtpForm(@PathVariable("id") Long id, Model model)
    {
        User user = userServices.findById(id);
        model.addAttribute("user", user);
        return "/user/forgotPassword/verifyOtp";
    }

    @PostMapping("/verifyOtp/{id}")
    public String verifyOtp(@PathVariable("id") Long id, @RequestParam("otp") Integer otp, Model model) {
        User user = userServices.findById(id);
        model.addAttribute("user", user);
        if(user != null) {
            ForgotPassword fp = forgotPasswordService.findInfoByOTPAndUser(user.getForgotPassword().getOtp(), user);
            Date currentTime = new Date();
            if(fp.getExpiredTime().before(currentTime)) {
                model.addAttribute("ExpiredOtp", "Otp hết hạn");
                forgotPasswordService.delete(fp);
                return "/user/forgotPassword/verifyOtp";
            }
            if(otp != fp.getOtp()){
                model.addAttribute("InvalidOtp", "OTP Không hợp lệ");
                return "/user/forgotPassword/verifyOtp";
            }
            forgotPasswordService.delete(fp);
            return "redirect:/user/forgotPassword/changePassword/" + id;
        }
        return "/user/forgotPassword/verifyOtp";
    }

    @GetMapping("/changePassword/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userServices.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "/user/forgotPassword/changePassword";
        }
        return "redirect:/login";
    }

    @PostMapping("/changePassword/{id}")
    public String update(@PathVariable("id") Long id, @Validated(OnUpdate.class) @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "/user/forgotPassword/changePassword";
        }
        User currentUser = userServices.findById(id);
        user.setId(id);
        user.setUserName(currentUser.getUserName());
        user.setEmail(currentUser.getEmail());
        user.setLocked(currentUser.isLocked());
        user.setName(currentUser.getName());
        user.setPhone(currentUser.getPhone());
        user.setTuoi(currentUser.getTuoi());
        user.setOauth(currentUser.isOauth());
        user.setPremiumDuration(currentUser.getPremiumDuration());
        user.setImageUrl(currentUser.getImageUrl());
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            result.rejectValue("password", "error.user", "Yêu cầu nhập mật khẩu mới");
            return "/user/forgotPassword/changePassword";
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.user", "Mật khẩu mới và xác nhận mật khẩu không khớp");
            return "/user/forgotPassword/changePassword";
        }
        boolean isAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
        if (isAdmin) {
            userServices.saveAdmin(user);
        } else {
            userServices.save(user);
        }
        return "redirect:/user/login";
    }

    private int otpGenerator(){
        Random rand = new Random();
        return rand.nextInt(100000, 999999);
    }
}