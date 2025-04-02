package EVC.Movie.controller.user;

import EVC.Movie.entity.User;
import EVC.Movie.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/membership")
public class MembershipController {
    @Autowired
    UserServices userServices;

    @GetMapping("/{userid}")
    public String membership(@PathVariable long userid, Model model) {
        User currentUser = userServices.findById(userid);
        model.addAttribute("user", currentUser);
        return "/user/userInfo/membership";
    }

    @PostMapping("/subscribe")
    public String subscribe(
            @RequestParam int planDuration,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        User currentUser = userServices.findByUsername(principal.getName());
        redirectAttributes.addAttribute("planDuration", planDuration);
        redirectAttributes.addAttribute("userid", currentUser.getId());
        return "redirect:/vnpay/submitOrder";
    }
}
