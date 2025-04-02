package EVC.Movie.api;

import EVC.Movie.entity.*;
import EVC.Movie.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiMovieController {
    @Autowired
    private MovieServices movieService;
    @Autowired
    private GenreServices genreServices;
    @Autowired
    private CountryServices countryServices;
    @Autowired
    private UserServices userServices;
    @Autowired
    private EpisodeServices episodeServices;
    @Autowired
    private CommentServices commentServices;
    @Autowired
    private LoveListServices loveListServices;
    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getALl();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/moviestop10")
    public ResponseEntity<List<Movie>> getAllMoviesTop10() {
        List<Movie> dsPage = movieService.pageGetAllWithRateAndView();
        return ResponseEntity.ok(dsPage);
    }
    @GetMapping("/movietop1")
    public ResponseEntity<Movie> getAllMovieTop1() {
        Movie dsPage = movieService.pageGetAllWithRateAndView().get(0);
        return ResponseEntity.ok(dsPage);
    }
    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") Long movieId) {
        Movie movie = movieService.getById(movieId);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/genres")
    public ResponseEntity<List<Genre>> getAllGenres() {
        List<Genre> genres = genreServices.getAll();
        return ResponseEntity.ok(genres);
    }
    @GetMapping("/countries")
    public ResponseEntity<List<Country>> getAllCountries() {
        List<Country> countries = countryServices.getAll();
        return ResponseEntity.ok(countries);
    }
    @GetMapping("/genres/{id}")
    public ResponseEntity<List<Genre>> getGenresByMovieId(@PathVariable("id") Long movieId) {
        List<Genre> genres = movieService.getGenresInMovie(movieId);
        if (genres != null && !genres.isEmpty()) {
            return ResponseEntity.ok(genres);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/movies/search")
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam("query") String query) {
        List<Movie> movies = movieService.pageGetAllMovieWithName(query);
        if (movies != null && !movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/filter")
    public ResponseEntity<?> filterMovies(
            @RequestParam(value = "genreId", required = false) Long genreId,
            @RequestParam(value = "countryId", required = false) Long countryId) {
        List<Movie> movies;
        if(countryId != null && genreId == null){
            movies = movieService.getByCountryId(countryId);
        }else if(genreId != null && countryId == null){
            movies = movieService.getByGenreId(genreId);
        }
        else if(genreId != null && countryId != null){
            movies = movieService.getByGenreAndCountry(genreId,countryId);
        }
        else {
            movies = movieService.getALl();
        }
        if (movies != null && !movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllMovies(@RequestParam("epId") Long epId) {
        List<Comment> comments = commentServices.findByEpisodeId(epId);
        return ResponseEntity.ok(comments);
    }
    @PostMapping("/luuyeuthich")
    public ResponseEntity<String> luuYT(@RequestBody Map<String, Object> payload) {
        String username = (String) payload.get("username");
        Long movieId = Long.valueOf(payload.get("movieId").toString());

        // Lấy thông tin phim từ ID
        Movie movie = movieService.getById(movieId);
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found");
        }

        // Lấy thông tin người dùng từ username
        User currentUser = userServices.findByUsername(username);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Kiểm tra xem đã lưu yêu thích chưa
        List<LoveList> existingLoveList = loveListServices.findByUserAndMovie(currentUser, movie);
        if (!existingLoveList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Movie already in love list");
        }

        // Lưu phim vào danh sách yêu thích
        LoveList loveMovie = new LoveList();
        loveMovie.setMovie(movie);
        loveMovie.setNgayluu(LocalDate.now());
        loveMovie.setUser(currentUser);
        loveListServices.save(loveMovie);

        return ResponseEntity.ok("Đã lưu yêu thích");
    }
}
