package EVC.Movie.repository;



import EVC.Movie.entity.LoveList;
import EVC.Movie.entity.Movie;
import EVC.Movie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILoveListRepository extends JpaRepository<LoveList,Long> {
    List<LoveList> findByUser(User currentUser);

    List<LoveList> findByUserAndMovie(User currentUser, Movie movie);

    List<LoveList> findByMovie_MovieId(Long movie);

    LoveList findByMovie_MovieIdAndUser(Long movieId, User currentUser);
}
