package EVC.Movie.controller;


import EVC.Movie.entity.Country;
import EVC.Movie.entity.Genre;
import EVC.Movie.entity.User;
import EVC.Movie.services.CountryServices;
import EVC.Movie.services.GenreServices;
import EVC.Movie.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final CountryServices countryServices;
    private final GenreServices genreServices;
    private final UserServices userServices;

    @Autowired
    public GlobalControllerAdvice(CountryServices countryServices, GenreServices genreServices, UserServices userServices) {
        this.countryServices = countryServices;
        this.genreServices = genreServices;
        this.userServices = userServices;
    }

    @ModelAttribute
    public void addAttributes(Model model, Locale locale) {
        List<Country> countries = countryServices.getAll();
        List<Genre> genres = genreServices.getAll();
        List<String> localizedGenreNames = genres.stream()
                .map(genre -> locale.getLanguage().equals("en") ? genre.getTransName() : genre.getName())
                .collect(Collectors.toList());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null ;
        if (authentication != null || authentication.isAuthenticated() || !authentication.getPrincipal().equals("anonymousUser")) {
            currentUser = userServices.findByUsername(authentication.getName());
        }
        model.addAttribute("countryList", countries);
        model.addAttribute("genres", genres);
        model.addAttribute("localizedGenreNames", localizedGenreNames);
        model.addAttribute("currentUserG",currentUser);
    }
}
