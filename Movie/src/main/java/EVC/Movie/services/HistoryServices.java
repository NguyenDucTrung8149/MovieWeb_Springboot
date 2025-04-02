package EVC.Movie.services;


import EVC.Movie.entity.Episode;
import EVC.Movie.entity.History;
import EVC.Movie.entity.Movie;
import EVC.Movie.entity.User;
import EVC.Movie.repository.IHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryServices {
    @Autowired
    private IHistoryRepository historyRepository;

    public List<History> getAll () {
        return historyRepository.findAll();
    }

    public History getById(Long id) {
        return historyRepository.findById(id).orElse(null);
    }

    public History save(History history) {
        return historyRepository.save(history);
    }

    public void delete(Long id) {
        historyRepository.deleteById(id);
    }

    public List<History> getAllByUser(User currentUser) {
        return historyRepository.findByUser(currentUser);
    }

    public History findByUserAndMovie(User currentUser, Long movieId) {
        return historyRepository.findByUserAndEpisode_Movie_MovieId(currentUser, movieId);
    }


}
