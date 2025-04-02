package EVC.Movie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên quốc gia không được để trống")
    @Size(max = 50, message = "Tên quốc gia phải ít hơn 50 ký tự")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Tên quốc gia không được để trống")
    @Size(max = 50, message = "Tên quốc gia phải ít hơn 50 ký tự")
    @Column(name = "nameanother")
    private String nameanother;

    @OneToMany(mappedBy = "country")
    @JsonIgnore
    private List<Movie> movies;
}
