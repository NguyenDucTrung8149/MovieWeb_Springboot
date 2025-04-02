package EVC.Movie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Data
@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genreId;

    @NotBlank(message = "Tên thể loại không được để trống")
    @Size(max = 50, message = "Tên thể loại phải ít hơn 50 ký tự")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Tên thể loại không được để trống")
    @Size(max = 50, message = "Tên thể loại (tiếng Anh) phải ít hơn 50 ký tự")
    @Column(name = "transName")
    private String transName;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "theloai_movie",
            joinColumns = @JoinColumn(name = "theloai_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    @JsonIgnore
    private Set<Movie> movies = new HashSet<>();

    public String getLocalizedName(Locale locale) {
        return locale.getLanguage().equals("en") ? this.transName : this.name;
    }
}
