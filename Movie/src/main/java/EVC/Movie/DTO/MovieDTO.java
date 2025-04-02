package EVC.Movie.DTO;

import EVC.Movie.entity.Episode;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieDTO {
    private Long movieId;
    private String name;
    private String nameanother;
    private String description;
    private String studio;
    private LocalDate timeupdate;
    private int duration;
    private boolean ismovie;
    private boolean state;
    private boolean premium;
    private String imageUrl;
    private String trailerUrl;
    private double average;
    private long view;
    private String country;
    private List<String> genres;
    private List<String> recomments;
    private List<EpisodeDTO> episodes;
}