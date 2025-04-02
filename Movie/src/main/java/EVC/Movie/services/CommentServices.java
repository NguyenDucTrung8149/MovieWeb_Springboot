package EVC.Movie.services;

import EVC.Movie.entity.Comment;
import EVC.Movie.repository.ICommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServices {
    @Autowired
    private ICommentRepository commentRepository;

    public List<Comment> getAll () {
        return commentRepository.findAll();
    }

    public Comment getById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> findByEpisodeId(Long episodeId) {
        return commentRepository.findByEpisodeId(episodeId);
    }

    public Page<Comment> findCommentByMovieIdAndUserId(Long movieId, Long userId, Pageable pageable) {
        return commentRepository.findByEpisodeMovieMovieIdAndUserId(movieId, userId, pageable);
    }

    public Page<Comment> findCommentByContent(String content, Pageable pageable) {
        return commentRepository.findByContentContainingIgnoreCase(content, pageable);
    }

    public Page<Comment> findPageCommentByMovie(Long movieId, Pageable pageable) {
        return commentRepository.findByEpisodeMovieMovieId(movieId, pageable);
    }

    public Page<Comment> findPageCommentByUserId(Long userId, Pageable pageable) {
        return commentRepository.findByUserId(userId, pageable);
    }

    public Page<Comment> getAllWithPagination(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }
    public List<Comment> findCommentByMovieId(Long movieId) {
        return commentRepository.findByEpisodeMovieMovieId(movieId);
    }

}
