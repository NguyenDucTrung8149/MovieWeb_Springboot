package EVC.Movie.DTO;

import EVC.Movie.entity.Episode;
import EVC.Movie.entity.Genre;
import EVC.Movie.entity.Movie;
import EVC.Movie.entity.Recomment;

import java.util.stream.Collectors;

public class MovieMapper {

    public static MovieDTO toDTO(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setMovieId(movie.getMovieId());
        dto.setName(movie.getName());
        dto.setNameanother(movie.getNameanother());
        dto.setDescription(movie.getDescription());
        dto.setStudio(movie.getStudio());
        dto.setTimeupdate(movie.getTimeupdate());
        dto.setDuration(movie.getDuration());
        dto.setIsmovie(movie.isIsmovie());
        dto.setState(movie.isState());
        dto.setPremium(movie.isPremium());
        dto.setImageUrl(movie.getImageUrl());
        dto.setTrailerUrl(movie.getTrailerUrl());
        dto.setAverage(movie.getAverage());
        dto.setView(movie.getView());
        dto.setCountry(movie.getCountry() != null ? movie.getCountry().getName() : null);
        dto.setGenres(movie.getGenres().stream().map(Genre::getName).collect(Collectors.toList()));
        dto.setRecomments(movie.getRecomments().stream().map(Recomment::getName).collect(Collectors.toList()));
        dto.setEpisodes(movie.getEpisodes().stream().map(MovieMapper::toEpisodeDTO).collect(Collectors.toList()));
        return dto;
    }

    private static EpisodeDTO toEpisodeDTO(Episode episode) {
        EpisodeDTO dto = new EpisodeDTO();
        dto.setId(episode.getId());
        dto.setDescription(episode.getDescription());
        dto.setEpisodeLink(episode.getEpisodeLink());
        dto.setTimeUpdate(episode.getTimeupdate());
        dto.setState(episode.getState());
        return dto;
    }
}
