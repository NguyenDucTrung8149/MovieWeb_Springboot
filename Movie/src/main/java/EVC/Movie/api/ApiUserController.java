package EVC.Movie.api;

import EVC.Movie.entity.*;
import EVC.Movie.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiUserController {
    @Autowired
    private UserServices userServices;
    @Autowired
    private LoveListServices loveListServices;
    @Autowired
    private MovieServices movieServices;
    @Autowired
    private EpisodeServices episodeServices;
    @Autowired
    private CommentServices commentServices;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        User user = userServices.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Sai tên đăng nhập hoặc mật khẩu"));
        }

        // Lấy danh sách loveLists
        List<LoveList> loveLists = loveListServices.getAllByUser(user);

        Map<String, Object> response = Map.of(
                "message", "Đăng nhập thành công",
                "user", Map.of(
                        "id", user.getId(),
                        "username", user.getUserName(),
                        "email", user.getEmail(),
                        "name", user.getName(),
                        "phone", user.getPhone(),
                        "tuoi", user.getTuoi(),
                        "imageUrl", user.getImageUrl() != null ? user.getImageUrl() : "",
                        "loveLists", loveLists
                )
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/register", consumes = {"application/json"})
    public ResponseEntity<?> register(@RequestBody Map<String, String> registrationRequest) {
        String username = registrationRequest.get("username");
        String password = registrationRequest.get("password");
        String email = registrationRequest.get("email");
        String name = registrationRequest.get("name");
        String phone = registrationRequest.get("phone");
        Integer tuoi = registrationRequest.containsKey("tuoi") ? Integer.valueOf(registrationRequest.get("tuoi")) : null;

        if (username == null || username.isEmpty() ||
                password == null || password.isEmpty() ||
                email == null || email.isEmpty() ||
                phone == null || phone.isEmpty() ||
                name == null || name.isEmpty() ||
                tuoi == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Thiếu thông tin bắt buộc"));
        }

        // Kiểm tra username hoặc email đã tồn tại
        if (userServices.findByUsername(username) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Tên đăng nhập đã tồn tại"));
        }
        if (userServices.findFirstbyEmail(email) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email đã tồn tại"));
        }

        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setName(name);
        user.setPhone(phone);
        user.setTuoi(tuoi);

        userServices.save(user);
        return ResponseEntity.ok(Map.of("message", "Đăng ký thành công", "userId", user.getId()));
    }

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@RequestBody Map<String, Object> requestBody) {
        try {
            // Lấy dữ liệu từ request body
            String username = (String) requestBody.get("username");
            Long episodeId = Long.valueOf(requestBody.get("episodeId").toString());
            String content = (String) requestBody.get("content");
            int rating = Integer.parseInt(requestBody.get("rating").toString());
            Long movieId = Long.valueOf(requestBody.get("movieId").toString());

            // Kiểm tra người dùng
            User user = userServices.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Kiểm tra episode
            Episode episode = episodeServices.getById(episodeId);
            if (episode == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Episode not found");
            }

            // Kiểm tra movie
            Movie movie = movieServices.getById(movieId);
            if (movie == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found");
            }

            // Kiểm tra nội dung bình luận
            if (content == null || content.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content cannot be blank");
            }

            // Tạo comment mới
            Comment comment = new Comment();
            comment.setUser(user);
            comment.setEpisode(episode);
            comment.setContent(content);
            comment.setTimeStamp(new Date());
            comment.setRating(rating);

            // Lưu comment
            commentServices.save(comment);

            // Cập nhật đánh giá trung bình cho phim
            movie.calculateAverageRating();
            movieServices.save(movie);

            return ResponseEntity.ok("Comment added successfully");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid rating or movieId");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
    @PostMapping("/comments/delete")
    public ResponseEntity<?> deleteComment(@RequestBody Map<String, Object> requestBody) {
        try {
            Long commentId = Long.valueOf(requestBody.get("commentId").toString());
            Long movieId = Long.valueOf(requestBody.get("movieId").toString());

            Movie movie = movieServices.getById(movieId);
            if (movie == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found");
            }

            commentServices.delete(commentId);

            movie.calculateAverageRating();
            movieServices.save(movie);

            return ResponseEntity.ok("Comment deleted successfully and rating updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting comment: " + e.getMessage());
        }
    }

    @GetMapping("/lovelists")
    public ResponseEntity<List<LoveList>> loveList(@RequestParam("username") String username) {
        User user = userServices.findByUsername(username);
        if(user != null) {
            List<LoveList> loveLists = loveListServices.getAllByUser(user);
            if (loveLists != null && !loveLists.isEmpty()) {
                return ResponseEntity.ok(loveLists);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        else {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/lovelistByMovieId")
    public ResponseEntity<List<LoveList>> loveList(@RequestParam("username") String username,
                                                   @RequestParam("movieId") Long movieId)
    {
        User user = userServices.findByUsername(username);
        if(user != null) {
            Movie movie = movieServices.getById(movieId);
            List<LoveList> loveLists = loveListServices.findByUserAndMovie(user,movie);
            if (loveLists != null && !loveLists.isEmpty()) {
                return ResponseEntity.ok(loveLists);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        else {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/lovelist/delete")
    public ResponseEntity<?> xoaYeuThich(@RequestParam("listId") Long listId ) {
         loveListServices.delete(listId);
         return ResponseEntity.ok("Xóa thành công.");
    }
}
