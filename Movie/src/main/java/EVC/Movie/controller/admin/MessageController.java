package EVC.Movie.controller.admin;


import EVC.Movie.entity.Message;
import EVC.Movie.entity.User;
import EVC.Movie.services.MessageServices;
import EVC.Movie.services.MovieServices;
import EVC.Movie.services.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/message")
public class MessageController {
    @Autowired
    private MessageServices messageServices;
    @Autowired
    private MovieServices movieServices;
    @Autowired
    private UserServices userServices;
    @GetMapping
    public String Index(Model model,
                        @RequestParam(value = "selectedMovie", required = false) Long movieId,
                        @RequestParam(value = "state", required = false) Boolean state,
                        @RequestParam(value = "emergency", required = false) Boolean emergency,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "12") int size) {
        if (page < 0) {
            page = 0;
        }
        Page<Message> dsPage;
        if(movieId != null && state != null && emergency != null){
            dsPage = messageServices.getAllWithMovieIdAndTypeAndEmg(movieId,state,emergency,PageRequest.of(page, size));
        }else if(movieId != null && state != null){
            dsPage = messageServices.getAllWithMovieIdAndType(movieId,state,PageRequest.of(page, size));
        }else if(movieId != null && emergency != null){
            dsPage = messageServices.getAllWithMovieIdAndEmg(movieId,emergency,PageRequest.of(page, size));
        }
        else if(state != null && emergency != null){
            dsPage = messageServices.getAllWithTypeAndEmg(state,emergency,PageRequest.of(page, size));
        }
        else if (state !=null){
            dsPage = messageServices.getAllWithType(state, PageRequest.of(page, size));
        }else if(movieId != null){
            dsPage = messageServices.getAllWithMovieId(movieId, PageRequest.of(page, size));
        }else if(emergency != null){
            dsPage = messageServices.getAllWithEmergency(emergency,PageRequest.of(page, size));
        }
        else{
            dsPage = messageServices.getAllWithPagination(PageRequest.of(page, size));
        }
        model.addAttribute("movies", movieServices.getALl());
        model.addAttribute("messageList", dsPage);
        model.addAttribute("state", state);
        model.addAttribute("emergency", emergency);
        model.addAttribute("selectedMovie",movieId);
        return "admin/message/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("message", new Message());
        model.addAttribute("movies", movieServices.getALl());
        return "admin/message/add";
    }
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("message") Message message, BindingResult result, Model model){
        if (result.hasErrors()) {
            model.addAttribute("movies", movieServices.getALl());
            return "admin/message/add";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userServices.findByUsername(username);

        message.setUser(currentUser);
        messageServices.save(message);
        return "redirect:/admin/message";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Message message = messageServices.getById(id);
        if (message != null) {
            model.addAttribute("message", message);
            model.addAttribute("movies", movieServices.getALl());
            return "admin/message/edit";
        }
        return "redirect:/admin/message";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id, @Valid @ModelAttribute("message") Message message, BindingResult result, Model model){
        if (result.hasErrors()) {
            model.addAttribute("movies", movieServices.getALl());
            return "admin/message/edit";
        }
        message.setId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userServices.findByUsername(username);

        message.setUser(currentUser);
        messageServices.save(message);
        return "redirect:/admin/message";
    }
    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable("id") Long id, HttpServletRequest request) {
        messageServices.delete(id);
        return "redirect:" + request.getHeader("Referer");
    }
}
