package EVC.Movie.repository;


import EVC.Movie.entity.Genre;
import EVC.Movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IMovieRepository extends JpaRepository<Movie, Long> {


    Page<Movie> findByCountry_Id(Long countryId, Pageable pageable);
    List<Movie> findByCountry_Id(Long id);
    List<Movie> findByNameContainingIgnoreCase(String thongtin);
    Page<Movie> findByNameContainingIgnoreCaseOrStudioContainingIgnoreCase(String thongtin, String thongtin1, Pageable pageable);

    @Query("select g from Genre g where g.genreId in (select g2.genreId from Movie m join m.genres g2 where m.movieId = :movieId)")
    List<Genre> findGenresInMovie(@Param("movieId") Long movieId);

    @Query("select g from Genre g where g.genreId not in (select g2.genreId from Movie m join m.genres g2 where m.movieId = :movieId)")
    List<Genre> findGenresNotInMovie(@Param("movieId") Long movieId);

    @Query("select count(g) > 0 from Movie m join m.genres g where m.movieId = :movieId and g.genreId = :genreId")
    boolean existsGenreInMovie(Long movieId, Long genreId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM theloai_movie WHERE movie_id=:movieId AND theloai_id=:genreId",nativeQuery = true)
    void deleteGenresToMovie(@Param("movieId") Long movieId, @Param("genreId") Long genreId);

    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.genreId = :genreId")
    Page<Movie> findByGenre(@Param("genreId") Long genreId, Pageable pageable);
    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.genreId = :genreId")
    List<Movie> findByGenre(@Param("genreId") Long genreId);

    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.genreId = :genreId and m.country.id = :countryId")
    List<Movie> findByGenreAndCountry(@Param("genreId") Long genreId,@Param("countryId") Long countryId);
    @Query("SELECT m FROM Movie m JOIN m.genres g JOIN m.episodes e WHERE g.genreId = :genreId AND m.timeupdate <= CURRENT_DATE  group by m order by MAX(e.timeupdate) desc ")
    Page<Movie> findByGenreDesc(@Param("genreId") Long genreId, Pageable pageable);

    @Query("SELECT m FROM Movie m JOIN m.episodes e WHERE m.country.id =:countryId  AND m.timeupdate <= CURRENT_DATE group by  m order by MAX(e.timeupdate) desc ")
    Page<Movie> findByCountryDesc(@Param("countryId") Long countryId, Pageable pageable);

    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.genreId = :genreId AND m.country.id = :countryId AND m.state = :state")
    Page<Movie> findByGenreAndCountryAndState(@Param("genreId") Long genreId, @Param("countryId") Long countryId, @Param("state") Boolean state, Pageable pageable);

    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.genreId = :genreId AND m.country.id = :countryId")
    Page<Movie> findByGenreAndCountry(@Param("genreId") Long genreId, @Param("countryId") Long countryId, Pageable pageable);

    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.genreId = :genreId AND m.state = :state")
    Page<Movie> findByGenreAndState(@Param("genreId") Long genreId, @Param("state") Boolean state, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.timeupdate IS NOT NULL AND m.timeupdate <= CURRENT_DATE ORDER BY m.timeupdate DESC")
    Page<Movie> findTopMovies(Pageable pageable);

    @Query(value = "SELECT m FROM Movie m WHERE m.timeupdate <= CURRENT_DATE ORDER BY m.average DESC, m.timeupdate  DESC ")
    List<Movie> findTopMoviesSortedByTimeUpdateAndAverage(Pageable pageable);
    @Query("SELECT m FROM Movie m JOIN m.episodes e WHERE m.ismovie = true AND m.timeupdate <= CURRENT_DATE group by m ORDER BY max(e.timeupdate) DESC")
    Page<Movie> findAllByIsmovieTrue(Pageable pageable);
    @Query("SELECT m FROM Movie m JOIN m.episodes e WHERE m.ismovie = false AND m.timeupdate <= CURRENT_DATE group by m ORDER BY max(e.timeupdate) DESC")
    Page<Movie> findAllByIsmovieFalse(Pageable pageable);
    @Query("SELECT m FROM Movie m JOIN m.episodes e WHERE m.state = true AND m.timeupdate <= CURRENT_DATE group by m ORDER BY max(e.timeupdate) DESC")
    Page<Movie> findAllByStateTrue(Pageable pageable);
    @Query("SELECT m FROM Movie m JOIN m.episodes e WHERE m.state = false AND m.timeupdate <= CURRENT_DATE group by m ORDER BY max(e.timeupdate) DESC")
    Page<Movie> findAllByStateFalse(Pageable pageable);
    @Query("SELECT m FROM Movie m WHERE m.timeupdate > CURRENT_TIMESTAMP ORDER BY m.timeupdate DESC")
    Page<Movie> findAllWithUpdateTimeAfterNow(Pageable pageable);

    @Query("""
    SELECT m FROM Movie m
    WHERE (LOWER(m.name) LIKE LOWER(CONCAT('%', :thongtin, '%'))
       OR LOWER(m.nameanother) LIKE LOWER(CONCAT('%', :thongtin, '%'))
       OR LOWER(m.studio) LIKE LOWER(CONCAT('%', :thongtin, '%')))
       AND m.timeupdate <= CURRENT_DATE ORDER BY m.timeupdate DESC
    """)
    Page<Movie> findAllByNameContainingIgnoreCase(@Param("thongtin") String thongtin, Pageable pageable);
    @Query("""
    SELECT m FROM Movie m
    WHERE (LOWER(m.name) LIKE LOWER(CONCAT('%', :thongtin, '%'))
       OR LOWER(m.nameanother) LIKE LOWER(CONCAT('%', :thongtin, '%'))
       OR LOWER(m.studio) LIKE LOWER(CONCAT('%', :thongtin, '%')))
       AND m.timeupdate <= CURRENT_DATE ORDER BY m.timeupdate DESC
    """)
    List<Movie> findAllByNameContainingIgnoreCase(@Param("thongtin") String thongtin);

    @Query("SELECT m FROM Movie m WHERE m.genres IS EMPTY")
    List<Movie> getMovieNotGenre();

    Page<Movie> findByState(Boolean state,Pageable pageable);

    @Query("select m from Movie m where m.episodes is empty ")
    List<Movie> getMovieEpisodesNull();

    @Query("SELECT m FROM Movie m ORDER BY m.average DESC")
    Page<Movie> findAllWithRateDesc(Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.timeupdate >= :oneMonthAgo ORDER BY m.view DESC")
    Page<Movie> findTop10MoviesByViewAndRecentUpdate(@Param("oneMonthAgo") LocalDate oneMonthAgo,Pageable pageable);
    @Query("SELECT m FROM Movie m ORDER BY m.view DESC")
    List<Movie> findTopViewedMovies(Pageable pageable);
    @Query("""
        SELECT m
        FROM Movie m
        JOIN m.episodes e
        WHERE m.average != 0
        GROUP BY m.movieId
        HAVING MAX(e.timeupdate) >= :oneMonthAgo
        ORDER BY m.average desc,m.view desc
    """)
    Page<Movie> findMoviesByRecentUpdate(@Param("oneMonthAgo") LocalDate oneMonthAgo, Pageable pageable);
    @Query("""
        SELECT m
        FROM Movie m
        JOIN m.episodes e
        WHERE m.average != 0
        GROUP BY m.movieId
        HAVING MAX(e.timeupdate) >= :oneMonthAgo
        ORDER BY m.average desc,m.view desc
    """)
    List<Movie> findMoviesByRecentUpdate(@Param("oneMonthAgo") LocalDate oneMonthAgo);
    @Query("SELECT m FROM Movie m JOIN m.episodes e WHERE m.timeupdate <= CURRENT_DATE group by m ORDER BY max(e.timeupdate) DESC")
    Page<Movie> findAllByDesc(Pageable pageable);
}
