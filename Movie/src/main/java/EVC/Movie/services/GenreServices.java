package EVC.Movie.services;

import EVC.Movie.entity.Genre;
import EVC.Movie.repository.IGenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class GenreServices {
    @Autowired
    private IGenreRepository genreRepository;

    public List<Genre> getAll () {
        return genreRepository.findAll();
    }

    public Genre getById(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

    public Genre save(Genre category) {
        return genreRepository.save(category);
    }

    public void delete(Long id) {
        genreRepository.deleteById(id);
    }
    public Page<Genre> findByName(String tenGenre, Pageable pageable) {
        return genreRepository.findByNameContainingIgnoreCase(tenGenre, pageable);
    }

    public Page<Genre> getAllWithPagination(Pageable pageable) {
        return genreRepository.findAll(pageable);
    }

    public String getLocalizedGenreName(Genre genre, Locale locale) {
        if (locale.getLanguage().equals("vi")) {
            return genre.getName();
        } else if (locale.getLanguage().equals("en")) {
            return genre.getTransName();
        } else {
            return genre.getName();
        }
    }
}
