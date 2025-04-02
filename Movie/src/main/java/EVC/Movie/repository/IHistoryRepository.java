package EVC.Movie.repository;


import EVC.Movie.entity.History;
import EVC.Movie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IHistoryRepository extends JpaRepository<History,Long> {
    List<History> findByUser(User currentUser);
    History findByUserAndEpisode_Movie_MovieId(User currentUser, Long movieId);

    List<History> findByUserId(Long userId);
}
