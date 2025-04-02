package EVC.Movie.repository;


import EVC.Movie.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IEpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findAllByMovie_MovieIdAndState(Long id,boolean state);

    List<Episode> findByMovie_MovieIdAndDescriptionContainingIgnoreCase(Long id, String thongTinTap);

    @Query("SELECT e FROM Episode e ORDER BY e.id DESC LIMIT 1")
    Episode getLast();

    List<Episode> findAllByMovie_MovieId(Long id);
}
