package EVC.Movie.controller.user;


import EVC.Movie.entity.ForgotPassword;
import EVC.Movie.entity.Message;
import EVC.Movie.entity.Movie;
import EVC.Movie.entity.User;
import EVC.Movie.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Autowired
    private  MovieServices movieServices;
    @Autowired
    private MessageServices messageServices;
    @Autowired
    private UserServices userServices;
    @Autowired
    private ForgotPasswordService forgotPasswordService;
    @GetMapping()
    public String Home(Model model ,Locale locale){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            User user = userServices.findByUsername(authentication.getName());
            ForgotPassword existingFp = forgotPasswordService.findInfoByUser(user);
            if(existingFp != null) {
                forgotPasswordService.delete(existingFp);
            }
        }
        List<Movie> moviesRecomment = movieServices.getLatestMovies(4);
        List<Movie> moviesNewest = movieServices.getLatestMovies(8);
        List<Movie> moviesAverage = movieServices.getLatestMoviesSortedByAverage(8);
        model.addAttribute("moviesRecomment",moviesRecomment);
        model.addAttribute("moviesNewest",moviesNewest);
        model.addAttribute("moviesAverage",moviesAverage);
        model.addAttribute("locale",locale);
        return "user/home";
    }
    @GetMapping("/thongbao")
    public String notification(Model model, Locale locale){
        List<Message> messagesEmg = messageServices.getAllWithEmgerTrueAndTypeFalse();
        List<Message> messages = messageServices.getAllWithEmgerFalseAndType();
        model.addAttribute("messagesEmg",messagesEmg);
        model.addAttribute("messages",messages);
        model.addAttribute("locale",locale);
        return "user/message";
    }
}
