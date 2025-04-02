package EVC.Movie.controller.user;

import EVC.Movie.entity.*;
import EVC.Movie.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/phim")
public class MovieController {
    @Autowired
    private MovieServices movieServices;
    @Autowired
    private GenreServices genreServices;
    @Autowired
    private EpisodeServices episodeServices;
    @Autowired
    private UserServices userService;
    @Autowired
    private LoveListServices loveListServices;
    @Autowired
    private HistoryServices historyServices;
    @Autowired
    private UserServices userServices;
    @Autowired
    private RecommentServices recommentServices;
    @Autowired
    private CommentServices commentServices;
    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private CountryServices countryServices;

    @GetMapping("chitiet/{movieId}")
    public String Chitiet(Model model, @PathVariable Long movieId,Locale locale) {
        List<Episode> episodes = episodeServices.getAllByMovieIdAndState(movieId);
        List<Comment> comments = commentServices.findCommentByMovieId(movieId);
        model.addAttribute("movie", movieServices.getById(movieId));
        model.addAttribute("episodes",episodes);
        if (!episodes.isEmpty()) {
            model.addAttribute("episodesLast", episodes.get(episodes.size() - 1));
        } else {
            model.addAttribute("episodesLast", null);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            String username = authentication.getName();
            User currentUser = userServices.findByUsername(username);
            List<Recomment> deXuats = recommentServices.getAllWithUser(currentUser);
            model.addAttribute("dexuats",deXuats);
            model.addAttribute("currentUser",authentication.getName());
        }else {
            model.addAttribute("dexuats",null);
        }
        List<Genre> genres = movieServices.getGenresInMovie(movieId);
        List<String> localizedGenreNames = genres.stream()
                .map(genre -> locale.getLanguage().equals("en") ? genre.getTransName() : genre.getName())
                .collect(Collectors.toList());
        model.addAttribute("comments", comments);
        model.addAttribute("genreList",genres);
        model.addAttribute("localizedGenreName", localizedGenreNames);
        model.addAttribute("dexuat", new Recomment());
        model.addAttribute("locale",locale);
        return "user/movie/detail";
    }
    @GetMapping("xem/{movieId}/{epId}")
    public String Xem(Model model, @PathVariable Long epId, @PathVariable Long movieId,Locale locale,
                      HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Episode episode = episodeServices.getById(epId);
        Movie movie = movieServices.getById(movieId);
        List<Episode> episodes = episodeServices.getAllByMovieIdAndState(movieId);
        List<Comment> comments = commentServices.findByEpisodeId(epId);
        if(movie.isPremium() && (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser"))){
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username);
            if(movie.isPremium() && currentUser.getPremiumDuration() != null
                    && currentUser.getPremiumDuration().isBefore(LocalDateTime.now())){
                String referer = request.getHeader("Referer");
                return "redirect:" + referer;
            }

            History historyAMovie = historyServices.findByUserAndMovie(currentUser, movieId);

            if (historyAMovie == null) {
                History newHistory = new History();
                newHistory.setNgayxem(LocalDate.now());
                newHistory.setUser(currentUser);
                newHistory.setEpisode(episode);
                newHistory.setViewCount(1);
                historyServices.save(newHistory);
            } else {
                long viewCount = (historyAMovie.getViewCount()+1);
                historyAMovie.setUser(currentUser);
                historyAMovie.setEpisode(episode);
                historyAMovie.setNgayxem(LocalDate.now());
                historyAMovie.setViewCount(viewCount);
                historyServices.save(historyAMovie);
            }
            model.addAttribute("currentUser",authentication.getName());
        }
        Long nextEpId = null;
        for (int i = 0; i < episodes.size(); i++) {
            if (episodes.get(i).getId().equals(epId) && i < episodes.size() - 1) {
                nextEpId = episodes.get(i + 1).getId();
                break;
            }
        }
        model.addAttribute("episode", episode);
        model.addAttribute("comments", comments);
        model.addAttribute("movie", movie);
        model.addAttribute("episodes", episodes);
        model.addAttribute("nextEpId", nextEpId);
        model.addAttribute("locate",locale);
        model.addAttribute("recomendationList", recommendationService.getMovieList(movieId));
        return "user/movie/watch";

    }
    @GetMapping("/theloai/{genreId}")
    public String moviesTheLoai(Model model, Locale locale, @PathVariable Long genreId,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "12") int size) {
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> dsPage = movieServices.findByGenreAndDayDesc(genreId, pageable);
        for(Movie movie : dsPage.getContent()){
            if(movie.getEpisodes()!=null && !movie.getEpisodes().isEmpty()){
                List<Episode> episodes = movie.getEpisodes();
                Episode latestEpisode = episodes.stream()
                        .max(Comparator.comparing(Episode::getTimeupdate))
                        .orElse(null);
                if (latestEpisode != null) {
                    movie.setTimeupdate(latestEpisode.getTimeupdate());
                }
            }
        }
        Genre genre = genreServices.getById(genreId);
        model.addAttribute("movies", dsPage);
        model.addAttribute("namePage", genre.getLocalizedName(locale));
        model.addAttribute("locate",locale);
        return "user/movie/moviesGenre";
    }
    @GetMapping("/quocgia/{countryId}")
    public String moviesQuocGia(Model model, Locale locale, @PathVariable Long countryId,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "12") int size) {
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> dsPage = movieServices.findByCountryDesc(countryId, pageable);
        for(Movie movie : dsPage.getContent()){
            if(movie.getEpisodes()!=null && !movie.getEpisodes().isEmpty()){
                List<Episode> episodes = movie.getEpisodes();
                Episode latestEpisode = episodes.stream()
                        .max(Comparator.comparing(Episode::getTimeupdate))
                        .orElse(null);
                if (latestEpisode != null) {
                    movie.setTimeupdate(latestEpisode.getTimeupdate());
                }
            }
        }
        Country country = countryServices.getById(countryId);
        model.addAttribute("movies", dsPage);
        model.addAttribute("country",country);
        model.addAttribute("locate",locale);
        return "user/movie/moviesCountries";
    }

    @GetMapping("/bo")
    public String moviesBo(Model model, Locale locale,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "12") int size) {
        if (page < 0) {
            page = 0;
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Movie> dsPage = movieServices.pageGetAllWithNotIsMovie(pageable);
        for(Movie movie : dsPage.getContent()){
            if(movie.getEpisodes()!=null && !movie.getEpisodes().isEmpty()){
                List<Episode> episodes = movie.getEpisodes();
                Episode latestEpisode = episodes.stream()
                        .max(Comparator.comparing(Episode::getTimeupdate))
                        .orElse(null);
                if (latestEpisode != null) {
                    movie.setTimeupdate(latestEpisode.getTimeupdate());
                }
            }
        }
        String namePage = locale.getLanguage().equals("vi") ? "phim bộ": "series";
        model.addAttribute("movies", dsPage);
        model.addAttribute("namePage", namePage);
        model.addAttribute("type","bo");
        model.addAttribute("locate",locale);
        return "user/movie/movies";
    }

    @GetMapping("/le")
    public String moviesLe(Model model,Locale locale,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "12") int size) {
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> dsPage = movieServices.pageGetAllWithIsMovie(pageable);
        for(Movie movie : dsPage.getContent()){
            if(movie.getEpisodes()!=null && !movie.getEpisodes().isEmpty()){
                List<Episode> episodes = movie.getEpisodes();
                Episode latestEpisode = episodes.stream()
                        .max(Comparator.comparing(Episode::getTimeupdate))
                        .orElse(null);
                if (latestEpisode != null) {
                    movie.setTimeupdate(latestEpisode.getTimeupdate());
                }
            }
        }
        String namePage = locale.getLanguage().equals("vi") ? "phim lẻ": "movies";
        model.addAttribute("movies", dsPage);
        model.addAttribute("namePage", namePage);
        model.addAttribute("type", "le");
        model.addAttribute("locate",locale);
        return "user/movie/movies";
    }

    @GetMapping("/dangchieu")
    public String moviesDangChieu(Model model,Locale locale,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "12") int size) {
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> dsPage = movieServices.pageGetAllWithNotIsState(pageable);
        for(Movie movie : dsPage.getContent()){
            if(movie.getEpisodes()!=null && !movie.getEpisodes().isEmpty()){
                List<Episode> episodes = movie.getEpisodes();
                Episode latestEpisode = episodes.stream()
                        .max(Comparator.comparing(Episode::getTimeupdate))
                        .orElse(null);
                if (latestEpisode != null) {
                    movie.setTimeupdate(latestEpisode.getTimeupdate());
                }
            }
        }
        String namePage = locale.getLanguage().equals("vi") ? "phim đang chiếu": "showing";
        model.addAttribute("movies", dsPage);
        model.addAttribute("namePage", namePage);
        model.addAttribute("type","dangchieu");
        model.addAttribute("locate",locale);
        return "user/movie/movies";
    }

    @GetMapping("/hoanthanh")
    public String moviesHoanThanh(Model model,Locale locale,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "12") int size) {
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> dsPage = movieServices.pageGetAllWithIsState(pageable);
        for(Movie movie : dsPage.getContent()){
            if(movie.getEpisodes()!=null && !movie.getEpisodes().isEmpty()){
                List<Episode> episodes = movie.getEpisodes();
                Episode latestEpisode = episodes.stream()
                        .max(Comparator.comparing(Episode::getTimeupdate))
                        .orElse(null);
                if (latestEpisode != null) {
                    movie.setTimeupdate(latestEpisode.getTimeupdate());
                }
            }
        }
        String namePage = locale.getLanguage().equals("vi") ? "phim hoàn thành": "completed";
        model.addAttribute("movies", dsPage);
        model.addAttribute("namePage", namePage);
        model.addAttribute("type","hoanthanh");
        model.addAttribute("locate",locale);
        return "user/movie/movies";
    }
    @GetMapping("/lichchieu")
    public String moviesLichChieu(Model model, Locale locale) {
        List<Movie> allMovies = movieServices.getALl();

        // Loại trừ các phim đã hoàn thành và có tập cuối cùng chiếu cách đây hơn 1 tuần
        List<Movie> filteredMovies = allMovies.stream()
                .filter(movie -> {
                    // Bỏ qua nếu phim chưa có tập nào
                    if (movie.getEpisodes() == null || movie.getEpisodes().isEmpty()) {
                        return true; // Giữ phim chưa có tập
                    }

                    // Lấy tập cuối cùng
                    Episode lastEpisode = movie.getEpisodes().get(movie.getEpisodes().size() - 1);

                    // Kiểm tra thời gian cập nhật của tập cuối
                    if (lastEpisode.getTimeupdate() != null) {
                        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
                        return lastEpisode.getTimeupdate().isAfter(oneWeekAgo); // Chỉ giữ phim có tập cuối gần đây
                    }

                    return true; // Giữ nếu không có thông tin thời gian cập nhật
                })
                .toList();

        // Thêm thông tin thời gian tập cuối vào từng movie
        for (Movie movie : filteredMovies) {
            if (movie.getEpisodes() != null && !movie.getEpisodes().isEmpty()) {
                Episode lastEpisode = movie.getEpisodes().get(movie.getEpisodes().size() - 1);
                movie.setTimeupdate(lastEpisode.getTimeupdate());
            }
        }
        // Phân loại phim theo ngày trong tuần
        Map<String, List<Movie>> moviesByDay = new LinkedHashMap<>();
        String[] weekDays = {"ThuHai", "ThuBa", "ThuTu", "ThuNam", "ThuSau", "ThuBay", "ChuNhat"};
        for (String day : weekDays) {
            moviesByDay.put(day, new ArrayList<>());
        }
        for (Movie movie : filteredMovies) {
            if (movie.getTimeupdate() != null) {
                DayOfWeek dayOfWeek = movie.getTimeupdate().getDayOfWeek();
                String dayName = switch (dayOfWeek) {
                    case MONDAY -> "ThuHai";
                    case TUESDAY -> "ThuBa";
                    case WEDNESDAY -> "ThuTu";
                    case THURSDAY -> "ThuNam";
                    case FRIDAY -> "ThuSau";
                    case SATURDAY -> "ThuBay";
                    case SUNDAY -> "ChuNhat";
                };
                moviesByDay.get(dayName).add(movie);
            }
        }

        // Thêm danh sách vào model
        for (String day : weekDays) {
            model.addAttribute(day, moviesByDay.get(day));
        }

        model.addAttribute("locale", locale);
        return "user/movie/schedule";
    }


    @GetMapping("/sapchieu")
    public String moviesSapChieu(Model model,Locale locale,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "12") int size) {
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> dsPage = movieServices.pageGetAllWithNotLastestMovie(pageable);
        String namePage = locale.getLanguage().equals("vi") ? "phim sắp chiếu": "incoming";
        model.addAttribute("movies", dsPage);
        model.addAttribute("namePage", namePage);
        model.addAttribute("type","sapchieu");
        model.addAttribute("locate",locale);
        return "user/movie/movies";
    }
    @GetMapping("/top10")
    public String moviesTop10(Model model,Locale locale,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> dsPage = movieServices.pageGetAllWithRateAndView(pageable);
        for(Movie movie : dsPage.getContent()){
            if(movie.getEpisodes()!=null && !movie.getEpisodes().isEmpty()){
                List<Episode> episodes = movie.getEpisodes();
                Episode latestEpisode = episodes.stream()
                        .max(Comparator.comparing(Episode::getTimeupdate))
                        .orElse(null);
                if (latestEpisode != null) {
                    movie.setTimeupdate(latestEpisode.getTimeupdate());
                }
            }
        }
        String namePage = locale.getLanguage().equals("vi") ? "phim top 10": "top 10";
        model.addAttribute("movies", dsPage);
        model.addAttribute("namePage", namePage);
        model.addAttribute("type","top10");
        model.addAttribute("locate",locale);
        return "user/movie/movies";
    }

    @GetMapping("/timkiem")
    public String moviesTimKiem(Model model,HttpServletRequest request,
                                @RequestParam(required = false) String thongtin,
                                @RequestParam(required = false) String lang,
                                 Locale locale,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "12") int size) {
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> dsPage;
        if (thongtin != null && !thongtin.isEmpty()){
            dsPage = movieServices.pageGetAllWithName(pageable,thongtin);
            for(Movie movie : dsPage.getContent()){
                if(movie.getEpisodes()!=null && !movie.getEpisodes().isEmpty()){
                    List<Episode> episodes = movie.getEpisodes();
                    Episode latestEpisode = episodes.stream()
                            .max(Comparator.comparing(Episode::getTimeupdate))
                            .orElse(null);
                    if (latestEpisode != null) {
                        movie.setTimeupdate(latestEpisode.getTimeupdate());
                    }
                }
            }
        } else {
            return "redirect:" + request.getHeader("Referer");
        }
        model.addAttribute("movies", dsPage);
        model.addAttribute("thongtin",thongtin);
        model.addAttribute("locate",locale);
        return "user/movie/moviesSearch";
    }

    @GetMapping("/luuyeuthich/{id}")
    public String luuYT (Model model, @PathVariable Long id, HttpServletRequest request){
        Movie movie = movieServices.getById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "/user/login";
        }
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);

        List<LoveList> existingLoveList = loveListServices.findByUserAndMovie(currentUser, movie);
        if (!existingLoveList.isEmpty()) {
            return "redirect:" + request.getHeader("Referer");
        }
        LoveList loveMovie = new LoveList();
        loveMovie.setMovie(movie);
        loveMovie.setNgayluu(LocalDate.now());
        loveMovie.setUser(currentUser);
        loveListServices.save(loveMovie);
        return "redirect:" + request.getHeader("Referer");
    }
}
