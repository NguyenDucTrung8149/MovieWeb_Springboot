package EVC.Movie.controller.user;

import EVC.Movie.entity.Message;
import EVC.Movie.entity.Movie;
import EVC.Movie.entity.User;
import EVC.Movie.services.MessageServices;
import EVC.Movie.services.MovieServices;
import EVC.Movie.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MovieApiController {
    @Autowired
    private MovieServices movieService;
    @Autowired
    private MessageServices messageServices;
    @Autowired
    private UserServices userServices;
    @PutMapping("/incrementView/{movieId}")
    public ResponseEntity<String> incrementView(@PathVariable Long movieId) {
        Movie movie = movieService.getById(movieId);
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Phim không tồn tại.");
        }
        movie.setMovieId(movieId);
        movie.setView(movie.getView() + 1);
        movieService.save(movie);
        return ResponseEntity.ok("Lượt xem đã được tăng.");
    }
    @PostMapping("/saveReport")
    public ResponseEntity<String> addMessage(@RequestParam("info") String info, @RequestParam("movieId") Long movieId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userServices.findByUsername(username);
        Movie movie = movieService.getById(movieId);
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Phim không tồn tại.");
        }
        Message message = new Message();
        message.setInfo(info);
        message.setType(true);
        message.setEmergency(true);
        message.setMovie(movie);
        message.setNgaydang(LocalDate.now());
        message.setUser(currentUser);
        messageServices.save(message);
        return ResponseEntity.ok("Gửi thành công.");
    }
}
