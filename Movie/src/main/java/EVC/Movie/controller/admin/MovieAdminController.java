package EVC.Movie.controller.admin;

import EVC.Movie.entity.*;
import EVC.Movie.entity.Message;
import EVC.Movie.services.*;
import com.amazonaws.services.s3.AmazonS3;
import com.google.firebase.messaging.FirebaseMessaging;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("admin/phim")
public class MovieAdminController {
    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private MovieServices movieServices;
    @Autowired
    private MessageServices messageServices;
    @Autowired
    private EpisodeServices episodeServices;
    @Autowired
    private GenreServices genreServices;

    @Autowired
    private CountryServices countryServices;
    @Autowired
    private UserServices userServices;
    @GetMapping
    public String Index(Model model, @RequestParam(required = false) String thongTin,
                        @RequestParam(value = "genreId", required = false) Long genreId,
                        @RequestParam(value = "countryId", required = false) Long countryId,
                        @RequestParam(value = "state", required = false) Boolean state,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "12") int size) {
        if (page < 0) {
            page = 0;
        }
        Page<Movie> dsPage;
        if (StringUtils.hasText(thongTin)) {
            genreId = null;
            countryId = null;
            state = null;
            dsPage = movieServices.findByNameAndStudio(thongTin, PageRequest.of(page, size));
        }
        else if (genreId != null && countryId != null && state != null) {
            dsPage = movieServices.findByGenreAndCountryAndState(genreId, countryId,state, PageRequest.of(page, size));
        }
        else if (genreId != null && countryId != null ) {
            dsPage = movieServices.findByGenreAndCountry(genreId, countryId, PageRequest.of(page, size));
        }
        else if (genreId != null && state != null) {
            dsPage = movieServices.findByGenreAndState(genreId, state, PageRequest.of(page, size));
        }
        else if (genreId != null) {
            dsPage = movieServices.findByGenre(genreId, PageRequest.of(page, size));
        }
        else if (countryId != null) {
            dsPage = movieServices.findByCountry(countryId, PageRequest.of(page, size));
        }
        else if(state != null){
            dsPage = movieServices.findByState(state, PageRequest.of(page, size));
        }
        else {
            dsPage = movieServices.getAllWithPagination(PageRequest.of(page, size));
        }
        List<Movie> MoviesEpNull = movieServices.getMovieEpisodesNull();
        List<Movie> MoviesGenreNull = movieServices.getMovieNotGenre();
        model.addAttribute("thongTin", thongTin);
        model.addAttribute("genres", genreServices.getAll());
        model.addAttribute("countries", countryServices.getAll());
        model.addAttribute("selectedGenre", genreId);
        model.addAttribute("selectedCountry", countryId);
        model.addAttribute("state", state);
        model.addAttribute("MoviesGenreNull", MoviesGenreNull);
        model.addAttribute("MoviesEpNull",MoviesEpNull);
        model.addAttribute("movies", dsPage);
        return "admin/movie/list";
    }


    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("countries", countryServices.getAll());
        return "admin/movie/add";
    }
    @PostMapping("/add")
    public String add(@RequestParam("imageUrl") MultipartFile image, @Valid @ModelAttribute("movie") Movie movie, BindingResult result, Model model) {
        if(result.hasFieldErrors("name")||result.hasFieldErrors("nameanother")
                ||result.hasFieldErrors("description") ||result.hasFieldErrors("studio")
                ||result.hasFieldErrors("timeupdate")||result.hasFieldErrors("duration"))
        {
            model.addAttribute("countries", countryServices.getAll());
            return "admin/movie/add";
        }
        else {
            try {
                if (!image.isEmpty()) {
                    // Upload ảnh lên bucket S3 dành cho ảnh
                    String imageUrl = s3Service.uploadFileToBucketImageMovie(image, movie.getMovieId());
                    movie.setImageUrl(imageUrl);
                }
            } catch (IOException e) {
                return "admin/movie/add";
            }
            if (movie.getCountry() == null || movie.getCountry().getId() == null) {
                movie.setCountry(null);
            }
            movieServices.save(movie);
        }
        return "redirect:/admin/phim";
    }


    @GetMapping("/edit/{id}")//movieId
    public String editForm(@PathVariable("id") Long id, Model model) {
        Movie movie = movieServices.getById(id);
        if (movie != null) {
            model.addAttribute("genres",movieServices.getGenresInMovie(id));
            model.addAttribute("movie", movie);
            model.addAttribute("countries", countryServices.getAll());
            return "admin/movie/edit";
        }
        return "redirect:/admin/phim";
    }

    @PostMapping("/edit/{id}")
    public String update(@RequestParam("imageUrl") MultipartFile image, @PathVariable("id") Long id, @Valid @ModelAttribute("movie") Movie movie, BindingResult result, Model model){
        if(result.hasFieldErrors("name")||result.hasFieldErrors("nameanother")
                ||result.hasFieldErrors("description") ||result.hasFieldErrors("studio")
                ||result.hasFieldErrors("timeupdate")||result.hasFieldErrors("duration"))
        {
            model.addAttribute("genres",movieServices.getGenresInMovie(id));
            model.addAttribute("countries", countryServices.getAll());
            return "admin/movie/edit";
        }
        Movie exisMovie = movieServices.getById(id);
        if(!image.isEmpty()){
            try{

                // Xóa file cũ trên S3 nếu tồn tại
                String oldFileUrl = exisMovie.getImageUrl();
                if (oldFileUrl != null && !oldFileUrl.isEmpty()) {
                    s3Service.deleteFileFromBucketImageMovie(oldFileUrl);
                }
                // Upload ảnh lên bucket S3 dành cho ảnh
                String imageUrl = s3Service.uploadFileToBucketImageMovie(image, id);
                movie.setImageUrl(imageUrl);
            }catch (IOException e){
                model.addAttribute("genres",movieServices.getGenresInMovie(id));
                model.addAttribute("countries", countryServices.getAll());
                return "admin/movie/edit";
            }
        }else{
            movie.setImageUrl(exisMovie.getImageUrl());
        }

        movie.setMovieId(id);
        if (movie.getCountry() == null || movie.getCountry().getId() == null) {
            movie.setCountry(null);
        }
        movieServices.save(movie);
        return "redirect:/admin/phim";
    }
    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable("id") Long id) {
        Movie movie = movieServices.getById(id);
        List<Episode> episodes = episodeServices.getAllByMovieId(movie.getMovieId());
        if(episodes!= null){
            for (Episode episode : episodes) {
                String episodeLink = episode.getEpisodeLink();
                if (episodeLink != null && !episodeLink.isEmpty()) {
                    try {
                        s3Service.deleteFile(episodeLink);
                        s3Service.deleteFilesBucketOutput(episode.getMovie().getMovieId(),episode.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                episodeServices.delete(episode.getId());
            }
        }

        if (movie.getImageUrl() != null && !movie.getImageUrl().isEmpty()) {
            s3Service.deleteFileFromBucketImageMovie(movie.getImageUrl());
        }
        movieServices.delete(id);
        return "redirect:/admin/phim";
    }



    //thể loại
    @GetMapping("/listgenre/{id}") //Id Movie
    public String listTheLoai(@PathVariable Long id,Model model,@RequestParam(required = false) String thongTin){
        List< Genre> genresInMovie = movieServices.getGenresInMovie(id);
        List<Genre> genresNotinMovie = movieServices.getGenresNotInMovie(id);
        if(StringUtils.hasText(thongTin)){
            genresInMovie = genresInMovie.stream().filter(m -> m.getName() != null && m.getName().toLowerCase().contains(thongTin.toLowerCase()))
                    .collect(Collectors.toList());
            genresNotinMovie = genresNotinMovie.stream().filter(m -> m.getName() != null && m.getName().toLowerCase().contains(thongTin.toLowerCase()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("movieId",id);
        model.addAttribute("genresInMovie",genresInMovie);
        model.addAttribute("genresNotinMovie",genresNotinMovie);
        return "admin/movie/listgenre";
    }
    //Thêm thể loại
    @GetMapping("/themtheloai/{movieId}/{genreId}")
    public String addGenresToMovie(@PathVariable Long genreId, @PathVariable Long movieId) {
        movieServices.addGenresToMovie(movieId,genreId);
        return "redirect:/admin/phim/listgenre/" + movieId;
    }
    //Xóa thể loại
    @GetMapping("/xoatheloai/{movieId}/{genreId}")
    public String deleteGenresToMovie(@PathVariable Long genreId, @PathVariable Long movieId, RedirectAttributes redirectAttributes) {
        movieServices.deleteGenresToMovie(movieId,genreId);
        return "redirect:/admin/phim/listgenre/" + movieId;
    }
    //Quản lý tập phim
    @GetMapping("/tap/{id}")
    public String listTapPhim(@PathVariable("id") Long id,
                              @RequestParam(value = "thongTinTap", required = false) String thongTinTap,
                              Model model)
    {
        List<Episode> episodes;
        if (thongTinTap != null && !thongTinTap.isEmpty()) {
            episodes = episodeServices.searchEpisodesByMovieIdAndKeyword(id, thongTinTap);
        } else {
            episodes = episodeServices.getAllByMovieId(id);
        }
        model.addAttribute("movieId",id);
        model.addAttribute("episodes", episodes);
        return "admin/episode/list";
    }

    @GetMapping("/tap/add/{id}")
    public String showAddForm(@PathVariable("id") Long id,Model model) {
        model.addAttribute("movieId", id);
        model.addAttribute("episode", new Episode());
        return "admin/episode/add";
    }
    @PostMapping("/tap/add/{id}") //id=movieId
    public String add(@PathVariable("id") Long id,@Valid @ModelAttribute("episode") Episode episode,BindingResult result,
                      @RequestParam("file") MultipartFile file,Model model) {
        if (result.hasErrors() || result.hasFieldErrors("description")) {
            model.addAttribute("episode", episode);
            model.addAttribute("movieId", id);
            return "admin/episode/add";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Movie movie = movieServices.getById(id);
        episode.setMovie(movie);
        episode.setState(false);
        episode.setTimeupdate(LocalDate.now());
        episodeServices.save(episode);

        Episode exisEpisode = episodeServices.getLast();
        if (!file.isEmpty()) {
            try {
                CompletableFuture<String> fileUrlFuture = s3Service.uploadFile(file, id, exisEpisode.getId());
                String fileUrl = fileUrlFuture.get();
                exisEpisode.setEpisodeLink(fileUrl);

                episodeServices.save(exisEpisode);
            } catch (IOException e) {
                result.rejectValue("episodeLink", "error.episode", "Không thể tải lên tệp.");
                return "admin/episode/add";
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        List<Episode> episodeList = episodeServices.getAllByMovieId(id);

        if(episodeList.size() >= movie.getDuration()){
            movie.setMovieId(id);
            movie.setState(true);
            movieServices.save(movie);
        }
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            String username = authentication.getName();
            User currentUser = userServices.findByUsername(username);
            // Thêm thông báo vào Message
            Message message = new Message();
            message.setInfo("Phim: " + episode.getMovie().getName() + " Đã cập nhật tập: " + episode.getDescription());
            message.setNgaydang(LocalDate.now());
            message.setUser(currentUser);
            message.setMovie(episode.getMovie());
            messageServices.save(message);

            messageServices.notifyUsersAboutNewEpisode(episode.getMovie(), message);
            sendNotificationToDevices(episode);
            return "redirect:/admin/phim/tap/{id}";
        }

        // Chuyển hướng về trang danh sách tập
        return "redirect:/admin/phim/tap/{id}";
    }

    public void sendNotificationToDevices(Episode episode) {
        try {
            String notificationTitle = "Phim mới được cập nhật!";
            String notificationBody = "Phim: " + episode.getMovie().getName() + " - Tập: " + episode.getDescription();

            // Tạo thông báo
            com.google.firebase.messaging.Message message = com.google.firebase.messaging.Message.builder()
                    .setTopic("new_episode")
                    .putData("title", notificationTitle)
                    .putData("body", notificationBody)
                    .putData("movieId", episode.getMovie().getMovieId().toString())
                    .build();

            // Gửi thông báo
            //FirebaseMessaging.getInstance().send(message);
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    @GetMapping("/tap/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Episode episode = episodeServices.getById(id);
        if (episode != null) {
            model.addAttribute("episode", episode);
            return "admin/episode/edit";
        }
        return "redirect:/admin/phim/tap/{id}";
    }
    @PostMapping("/tap/edit/{id}")
    public String update(@PathVariable("id") Long id, @Valid @ModelAttribute("episode") Episode episode,BindingResult result,
                         @RequestParam("file") MultipartFile file) {
        if (result.hasErrors()) {
            return "admin/episode/edit";
        }
        Episode exisEpisode = episodeServices.getById(id);
        if (!file.isEmpty()) {
            try {
                // Xóa file cũ trên S3 nếu tồn tại
                String oldFileUrl = exisEpisode.getEpisodeLink();
                if (oldFileUrl != null && !oldFileUrl.isEmpty()) {
                    s3Service.deleteFile(oldFileUrl);
                    s3Service.deleteFilesBucketOutput(exisEpisode.getMovie().getMovieId(),exisEpisode.getId());
                }

                // Tải lên file mới
                CompletableFuture<String> fileUrlFuture = s3Service.uploadFile(file, episode.getMovie().getMovieId(), episode.getId());
                String fileUrl = fileUrlFuture.get();
                episode.setEpisodeLink(fileUrl);

            } catch (IOException e) {
                e.printStackTrace();
                result.rejectValue("episodeLink", "error.episode", "Không thể tải lên tệp.");
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            episode.setEpisodeLink(exisEpisode.getEpisodeLink());
        }
        episode.setId(id);
        episodeServices.save(episode);
        return "redirect:/admin/phim/tap/"+ episode.getMovie().getMovieId();
    }
    @GetMapping("/tap/delete/{id}")
    public String deleteEpisode(@PathVariable("id") Long id) {
        Episode episode = episodeServices.getById(id);

        if (episode != null) {
            String episodeLink = episode.getEpisodeLink();
            if (episodeLink != null && !episodeLink.isEmpty()) {
                try {
                    s3Service.deleteFile(episodeLink);
                    s3Service.deleteFilesBucketOutput(episode.getMovie().getMovieId(),episode.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            episodeServices.delete(id);
            Long movieId = episode.getMovie().getMovieId();
            return "redirect:/admin/phim/tap/" + movieId;
        }

        return "redirect:/admin/phim";
    }
}
