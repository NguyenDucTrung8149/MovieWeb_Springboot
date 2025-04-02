package EVC.Movie.services;

import EVC.Movie.entity.Movie;
import EVC.Movie.entity.Recomment;
import EVC.Movie.entity.User;
import EVC.Movie.repository.IRecommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommentServices {
    @Autowired
    private IRecommentRepository recommentRepository;
    @Autowired
    private MovieServices movieServices;

    public List<Recomment> getAll () {
        return recommentRepository.findAll();
    }

    public Recomment getById(Long id) {
        return recommentRepository.findById(id).orElse(null);
    }

    public Recomment save(Recomment deXuat) {
        return recommentRepository.save(deXuat);
    }
    public void savedx(Recomment deXuat) {
        recommentRepository.save(deXuat);
    }
    public void delete(Long id) {

        recommentRepository.deleteById(id);
    }
    public Page<Recomment> findByName(String tenDexuat, Pageable pageable) {
        return recommentRepository.findByNameContainingIgnoreCaseAndUserNull(tenDexuat, pageable);
    }
    public Page<Recomment> getAllWithPagination(Pageable pageable) {
        return recommentRepository.findAll(pageable);
    }
    public Page<Recomment> getAllWithPaginationUserNull(Pageable pageable) {
        return recommentRepository.findAllByUserNull(pageable);
    }
    public List<Movie> getMoviesNotInDeXuat(Long deXuatId) {
        return recommentRepository.findMoviesNotInDeXuat(deXuatId);
    }
    public List<Movie> getMoviesInDeXuat(Long deXuatId) {
        return recommentRepository.findMoviesInDeXuat(deXuatId);
    }
    public void addMovieToDeXuat(Long deXuatId, Long movieId) {
        if (!recommentRepository.existsMovieInDeXuat(deXuatId, movieId)) {
            Recomment dexuat = getById(deXuatId);
            Movie movie = movieServices.getById(movieId);
            dexuat.getMovies().add(movie);
            recommentRepository.save(dexuat);
        }
    }
    public void deleteMovieToDeXuat(Long dexuatId, Long movieId) {
        recommentRepository.deleteMovieFromDeXuat(dexuatId, movieId);
    }

    public List<Recomment> getAllWithUser(User user) {
        return recommentRepository.findByUser(user);
    }
    public List<String> findFirstMovieImageByRecommentId(List<Recomment> recomments) {
        return recomments.stream()
                .map(recomment -> {
                    List<String> imageUrls = recommentRepository.findFirstMovieImageByRecommentId(recomment.getId());
                    return imageUrls.isEmpty() ? null : imageUrls.get(0);
                })
                .collect(Collectors.toList());
    }
}
