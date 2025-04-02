package EVC.Movie.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EpisodeDTO {
    private Long id;
    private String description;
    private String episodeLink;
    private LocalDate timeUpdate;
    private Boolean state;

}