package EVC.Movie.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 200, message = "Tên phải ít hơn 200 ký tự")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 200, message = "Tên phải ít hơn 200 ký tự")
    @Column(name = "nameanother")
    private String nameanother;

    @Size(max = 1000, message = "Mô tả phải ít hơn 1000 ký tự")
    @Column(name = "description")
    private String description;

    @NotBlank(message = "Studio không được để trống")
    @Size(max = 100, message = "Tên studio phải ít hơn 100 ký tự")
    @Column(name = "studio")
    private String studio;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Thời gian cập nhật (Thời gian chiếu)")
    @Column(name = "timeupdate")
    private LocalDate timeupdate;

    @NotNull(message = "Số tập phim không được để trống")
    @Column(name = "duration")
    private int duration;

    @Column(name = "ismovie")
    private boolean ismovie;

    @Column(name = "state")
    private boolean state;

    @Column(name="premium")
    private boolean premium = false;

    @Column(name = "image")
    private String imageUrl;

    @Column(name = "trailerurl")
    private String trailerUrl;

    @Column(name = "average")
    private double average = 0.0;

    @Column(name = "view")
    private long view = 0;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "theloai_movie",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "theloai_id")
    )
    @JsonManagedReference
    private Set<Genre> genres = new HashSet<>();
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "dexuat_movie",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "dexuat_id")
    )
    @JsonIgnore
    private Set<Recomment> recomments = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Episode> episodes;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<LoveList> loveLists;
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> messages;

    public void calculateAverageRating() {
        double totalRating = 0;
        int count = 0;

        for (Episode episode : episodes) {
            for (Comment comment : episode.getComments()) {
                totalRating += comment.getRating();
                count++;
            }

        }
        if (count > 0) {
            BigDecimal bd = BigDecimal.valueOf(totalRating / count);
            bd = bd.setScale(1, RoundingMode.HALF_UP);  // Làm tròn đến 1 chữ số thập phân
            this.average = bd.doubleValue();
        } else {
            this.average = 0.0;
        }
    }
}
