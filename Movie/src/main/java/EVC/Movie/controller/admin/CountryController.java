package EVC.Movie.controller.admin;

import EVC.Movie.entity.Country;
import EVC.Movie.entity.Movie;
import EVC.Movie.services.CountryServices;
import EVC.Movie.services.MovieServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin/quocgia")
public class CountryController {
    @Autowired
    private CountryServices countryServices;
    @Autowired
    private MovieServices movieServices;
    @GetMapping
    public String index(Model model, @RequestParam(required = false) String tenQuocGia,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
        if (page < 0) {
            page = 0;
        }
        Page<Country> dsCountryPage;
        if (StringUtils.hasText(tenQuocGia)) {
            dsCountryPage = countryServices.findByName(tenQuocGia, PageRequest.of(page, size));
        } else {
            dsCountryPage = countryServices.getAllWithPagination(PageRequest.of(page, size));
        }
        model.addAttribute("tenQuocGia", tenQuocGia);
        model.addAttribute("countries", dsCountryPage);
        return "admin/country/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("country", new Country());
        return "admin/country/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("country") Country country, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/country/add";
        }
        countryServices.save(country);
        return "redirect:/admin/quocgia";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Country country = countryServices.getById(id);
        if (country != null) {
            model.addAttribute("country", country);
            return "admin/country/edit";
        }
        return "redirect:/admin/quocgia";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id, @Valid @ModelAttribute("country") Country country, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/country/edit";
        }
        country.setId(id);
        countryServices.save(country);
        return "redirect:/admin/quocgia";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        List<Movie> movies = movieServices.findByCountryList(id);
        for (Movie movie : movies) {
            movie.setCountry(null);
            movieServices.save(movie);
        }
        countryServices.delete(id);
        return "redirect:/admin/quocgia";
    }
}
