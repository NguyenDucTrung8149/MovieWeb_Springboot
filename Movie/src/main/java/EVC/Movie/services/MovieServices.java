package EVC.Movie.services;

import EVC.Movie.entity.Genre;
import EVC.Movie.entity.Movie;
import EVC.Movie.repository.IMovieRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieServices {
    @Autowired
    private IMovieRepository movieRepository;
    @Autowired
    private GenreServices genreServices;

    public List<Movie> getALl() {
        return movieRepository.findAll();
    }
    public Movie getById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }
    public List<Movie> getLatestMovies(int limit) {
        return movieRepository.findTopMovies(PageRequest.of(0, limit)).getContent();
    }
    public List<Movie> getLatestMoviesSortedByAverage(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return movieRepository.findTopMoviesSortedByTimeUpdateAndAverage(pageable);
    }
    public void delete(Long id) {
        movieRepository.deleteById(id);
    }

    public Page<Movie> findByNameAndStudio(String thongtin, Pageable pageable) {
        return movieRepository.findByNameContainingIgnoreCaseOrStudioContainingIgnoreCase(thongtin, thongtin, pageable);
    }
    public Page<Movie> findByCountry(Long countryId, Pageable pageable) {
        return movieRepository.findByCountry_Id(countryId, pageable);
    }
    public List<Movie> findByCountryList(Long countryId) {
        return movieRepository.findByCountry_Id(countryId);
    }
    public Page<Movie> findByState(Boolean state, Pageable pageable) {
        return movieRepository.findByState(state,pageable);
    }
    public Page<Movie> getAllWithPagination(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }
    public Page<Movie> pageGetAllWithIsMovie(Pageable pageable) {
        return movieRepository.findAllByIsmovieTrue(pageable);
    }
    public Page<Movie> pageGetAllWithNotIsMovie(Pageable pageable) {
        return movieRepository.findAllByIsmovieFalse(pageable);
    }
    public Page<Movie> pageGetAllWithIsState(Pageable pageable) {
        return movieRepository.findAllByStateTrue(pageable);
    }
    public Page<Movie> pageGetAllWithNotIsState(Pageable pageable) {
        return movieRepository.findAllByStateFalse(pageable);
    }
    public Page<Movie> pageGetAllWithNotLastestMovie(Pageable pageable){
        return movieRepository.findAllWithUpdateTimeAfterNow(pageable);
    }
    public Page<Movie> pageGetAllWithRate(Pageable pageable){
        return movieRepository.findAllWithRateDesc(pageable);
    }
    public Page<Movie> pageGetAllWithRateAndView(Pageable pageable) {

        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);

        Page<Movie> moviesByRecentUpdate = movieRepository.findMoviesByRecentUpdate(oneMonthAgo, pageable);

        List<Movie> sortedMovies = moviesByRecentUpdate.getContent().stream()
                .limit(10)
                .collect(Collectors.toList());

        // Tạo một Page mới từ danh sách đã sắp xếp
        return new PageImpl<>(sortedMovies, pageable, sortedMovies.size());
    }
    public List<Movie> pageGetAllWithRateAndView() {

        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);

        List<Movie> moviesByRecentUpdate = movieRepository.findMoviesByRecentUpdate(oneMonthAgo);

        List<Movie> sortedMovies = moviesByRecentUpdate.stream()
                .limit(10)
                .collect(Collectors.toList());

        // Tạo một Page mới từ danh sách đã sắp xếp
        return sortedMovies;
    }
    public Page<Movie> pageGetAllWithName(Pageable pageable, String thongtin) {
        return movieRepository.findAllByNameContainingIgnoreCase(thongtin, pageable);
    }
    public List<Movie> pageGetAllMovieWithName(String thongtin) {
        return movieRepository.findAllByNameContainingIgnoreCase(thongtin);
    }
    public Page<Movie> pageGetAll(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }
    public Page<Movie> pageGetAllDesc(Pageable pageable) {
        return movieRepository.findAllByDesc(pageable);
    }
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }
    public List<Movie> getByCountryId(Long id) {
        return movieRepository.findByCountry_Id(id);
    }
    public List<Movie> getByGenreId(Long id) {
        return movieRepository.findByGenre(id);
    }
    public List<Movie> getByGenreAndCountry(Long genreId,Long countryId) {
        return movieRepository.findByGenreAndCountry(genreId,countryId);
    }
    public List<Genre> getGenresInMovie(Long MovieId) {
        return movieRepository.findGenresInMovie(MovieId);
    }
    public List<Genre> getGenresNotInMovie(Long MovieId) {
        return movieRepository.findGenresNotInMovie(MovieId);
    }

    public void addGenresToMovie(Long movieId, Long genreId) {
        if(!movieRepository.existsGenreInMovie(movieId,genreId)){
            Movie movie = getById(movieId);
            Genre genre = genreServices.getById(genreId);
            movie.getGenres().add(genre);
            movieRepository.save(movie);
        }
    }

    public void deleteGenresToMovie(Long movieId, Long genreId) {
        movieRepository.deleteGenresToMovie(movieId,genreId);
    }

    public Page<Movie> findByGenre(Long genreId, Pageable pageable) {
        return movieRepository.findByGenre(genreId,pageable);
    }
    public Page<Movie> findByGenreAndDayDesc(Long genreId, Pageable pageable) {
        return movieRepository.findByGenreDesc(genreId,pageable);
    }
    public Page<Movie> findByCountryDesc(Long genreId, Pageable pageable) {
        return movieRepository.findByCountryDesc(genreId,pageable);
    }

    public Page<Movie> findByGenreAndCountryAndState(Long genreId, Long countryId,Boolean state, Pageable pageable) {
        return movieRepository.findByGenreAndCountryAndState(genreId,countryId,state,pageable);
    }
    public Page<Movie> findByGenreAndCountry(Long genreId, Long countryId, Pageable pageable) {
        return movieRepository.findByGenreAndCountry(genreId,countryId,pageable);
    }
    public Page<Movie> findByGenreAndState(Long genreId, Boolean state, Pageable pageable) {
        return movieRepository.findByGenreAndState(genreId,state,pageable);
    }
    public List<Movie> getMovieNotGenre() {
        return movieRepository.getMovieNotGenre();
    }
    public  List<Movie> getMovieEpisodesNull(){
        return movieRepository.getMovieEpisodesNull();
    }

    public List<Movie> getTopViewedMovies(int limit) {
        return movieRepository.findTopViewedMovies(PageRequest.of(0, limit));
    }
}
