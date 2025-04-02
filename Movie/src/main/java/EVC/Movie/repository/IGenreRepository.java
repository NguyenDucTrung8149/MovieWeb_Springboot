package EVC.Movie.repository;

import EVC.Movie.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface IGenreRepository extends JpaRepository<Genre,Long> {
    Page<Genre> findByNameContainingIgnoreCase(String tenGenre, Pageable pageable);
    @Query("SELECT DISTINCT g.name FROM Genre g")
    List<String> findAllGenreNames();

    // Lấy danh sách genres theo movieId
    @Query("SELECT g.name FROM Genre g JOIN g.movies m WHERE m.movieId = :movieId")
    Set<String> findGenresByMovieId(@Param("movieId") Long movieId);
}
