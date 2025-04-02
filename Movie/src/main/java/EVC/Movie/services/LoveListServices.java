package EVC.Movie.services;


import EVC.Movie.entity.LoveList;
import EVC.Movie.entity.Movie;
import EVC.Movie.entity.User;
import EVC.Movie.repository.ILoveListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoveListServices {
    @Autowired
    private ILoveListRepository loveListRepository;

    public List<LoveList> getAll () {
        return loveListRepository.findAll();
    }

    public LoveList getById(Long id) {
        return loveListRepository.findById(id).orElse(null);
    }

    public LoveList save(LoveList loveList) {
        return loveListRepository.save(loveList);
    }

    public void delete(Long id) {
        loveListRepository.deleteById(id);
    }

    public List<LoveList> getAllByUser(User currentUser) {
        return loveListRepository.findByUser(currentUser);
    }

    public List<LoveList> findByUserAndMovie(User currentUser, Movie movie) {
        return loveListRepository.findByUserAndMovie(currentUser, movie);
    }
    public int sum(Long movie){
        return loveListRepository.findByMovie_MovieId(movie).size();
    }

    public LoveList getByMovieIdAndUser(Long movieId, User currentUser) {
        return loveListRepository.findByMovie_MovieIdAndUser(movieId,currentUser);
    }
}
