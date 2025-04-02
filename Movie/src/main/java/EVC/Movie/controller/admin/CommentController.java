package EVC.Movie.controller.admin;

import EVC.Movie.entity.Comment;
import EVC.Movie.entity.Episode;
import EVC.Movie.entity.Movie;
import EVC.Movie.entity.User;
import EVC.Movie.services.CommentServices;
import EVC.Movie.services.EpisodeServices;
import EVC.Movie.services.MovieServices;
import EVC.Movie.services.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentServices commentServices;

    @Autowired
    private UserServices userServices;
    @Autowired
    private MovieServices movieServices;

    @Autowired
    private EpisodeServices episodeServices;

    @GetMapping()
//    @PreAuthorize("hasAuthority('ADMIN')")
    public String list(Model model,
                       @RequestParam(required = false) String thongTin,
                       @RequestParam(value = "movieId", required = false) Long movieId,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "20") int size) {
        if (page < 0) {
            page = 0;
        }
        Page<Comment> dsComment;
        if (StringUtils.hasText(thongTin)) {
            dsComment = commentServices.findCommentByContent(thongTin, PageRequest.of(page, size));
        }
        else if (movieId != null) {
            dsComment = commentServices.findPageCommentByMovie(movieId, PageRequest.of(page, size));
        }
        else {
            dsComment = commentServices.getAllWithPagination(PageRequest.of(page, size));
        }
        model.addAttribute("thongtin", thongTin);
        model.addAttribute("movies", movieServices.getALl());
        model.addAttribute("users", userServices.findAll());
        model.addAttribute("selectedMovie", movieId);
        model.addAttribute("comments", dsComment);
        return "/admin/comment/list";
    }

    @PostMapping("/add")
    public String addComment(@RequestParam Long episodeId,
                             @RequestParam String content,
                             @RequestParam Integer star,
                             @RequestParam Long movieId,
                             Principal principal,
                             HttpServletRequest request) {
        if(principal == null){
            return "loginPre";
        }
        User user = userServices.findByUsername(principal.getName());
        Episode episode = episodeServices.getById(episodeId);
        Movie movie = movieServices.getById(movieId);
        if(user != null && episode != null) {
            Comment comment = new Comment();
            comment.setUser(user);
            comment.setEpisode(episode);
            comment.setContent(content);
            comment.setTimeStamp(new Date());
            comment.setRating(star);
            commentServices.save(comment);
        }
        movie.calculateAverageRating();
        movieServices.save(movie);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id, @RequestParam Long movieId, HttpServletRequest request) {
        Movie movie = movieServices.getById(movieId);
        commentServices.delete(id);
        movie.calculateAverageRating();
        movieServices.save(movie);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

}
