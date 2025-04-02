package EVC.Movie.services;


import EVC.Movie.entity.Episode;
import EVC.Movie.repository.IEpisodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class EpisodeServices {
    @Autowired
    private IEpisodeRepository episodeRepository;

    public List<Episode> getAll () {
        return episodeRepository.findAll();
    }

    public Episode getById(Long id) {
        return episodeRepository.findById(id).orElse(null);
    }
    public List<Episode> getAllByMovieIdAndState(Long id) {
        List<Episode> episodes = episodeRepository.findAllByMovie_MovieIdAndState(id,true);
        if(episodes != null){
            return episodes;
        }
        return null;
    }
    public List<Episode> getAllByMovieId(Long id) {
        List<Episode> episodes = episodeRepository.findAllByMovie_MovieId(id);
        if(episodes != null){
            return episodes;
        }
        return null;
    }
    public Episode save(Episode category) {
        return episodeRepository.save(category);
    }

    public void delete(Long id) {
        episodeRepository.deleteById(id);
    }

    public List<Episode> searchEpisodesByMovieIdAndKeyword(Long id, String thongTinTap) {
        return episodeRepository.findByMovie_MovieIdAndDescriptionContainingIgnoreCase(id, thongTinTap);
    }

    public Episode getLast() {
        return episodeRepository.getLast();
    }
}
