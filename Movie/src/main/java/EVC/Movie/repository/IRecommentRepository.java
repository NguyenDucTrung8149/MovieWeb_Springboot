package EVC.Movie.repository;


import EVC.Movie.entity.Movie;
import EVC.Movie.entity.Recomment;
import EVC.Movie.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IRecommentRepository extends JpaRepository<Recomment,Long> {
    Page<Recomment> findByNameContainingIgnoreCaseAndUserNull(String tenDexuat, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.movieId NOT IN (SELECT m2.movieId FROM Recomment d JOIN d.movies m2 WHERE d.id = :deXuatId)")
    List<Movie> findMoviesNotInDeXuat(@Param("deXuatId") Long deXuatId);

    @Query("SELECT m FROM Movie m WHERE m.movieId IN (SELECT m2.movieId FROM Recomment d JOIN d.movies m2 WHERE d.id = :deXuatId)")
    List<Movie> findMoviesInDeXuat(@Param("deXuatId") Long deXuatId);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM dexuat_movie WHERE dexuat_id = :dexuatId AND movie_id = :movieId", nativeQuery = true)
    void deleteMovieFromDeXuat(@Param("dexuatId") Long dexuatId, @Param("movieId") Long movieId);

    @Query("SELECT COUNT(m) > 0 FROM Recomment d JOIN d.movies m WHERE d.id = :danhsachId AND m.movieId = :movieId")
    boolean existsMovieInDeXuat(Long danhsachId, Long movieId);

    List<Recomment> findByUser(User user);

    Page<Recomment> findAllByUserNull(Pageable pageable);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM dexuat_movie WHERE dexuat_id = :id", nativeQuery = true)
    void deleteDeXuatMoviesByDeXuatId(@Param("id") Long id);
    @Modifying
    @Query("DELETE FROM Recomment d WHERE d.id = :id")
    void deleteDeXuatById(@Param("id") Long id);
    @Query("SELECT m.imageUrl " +
            "FROM Recomment r " +
            "LEFT JOIN r.movies m " +
            "WHERE r.id = :recommentId " +
            "ORDER BY m.movieId ASC")
    List<String> findFirstMovieImageByRecommentId(@Param("recommentId") Long recommentId);
}

