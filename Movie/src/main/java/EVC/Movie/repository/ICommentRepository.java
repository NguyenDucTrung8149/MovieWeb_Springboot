package EVC.Movie.repository;

import EVC.Movie.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEpisodeId(Long episodeId);
    Page<Comment> findByEpisodeMovieMovieIdAndUserId(Long movieId, Long userId, Pageable pageable);
    Page<Comment> findByContentContainingIgnoreCase(String content, Pageable pageable);
    Page<Comment> findByEpisodeMovieMovieId(Long movieId, Pageable pageable);
    Page<Comment> findByUserId(Long userId, Pageable pageable);
    List<Comment> findByEpisodeMovieMovieId(Long movieId);
}
