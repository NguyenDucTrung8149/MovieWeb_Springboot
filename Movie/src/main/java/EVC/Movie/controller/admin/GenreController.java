package EVC.Movie.controller.admin;

import EVC.Movie.entity.Genre;
import EVC.Movie.services.GenreServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/theloai")
public class GenreController {
    @Autowired
    private GenreServices genreServices;

    @GetMapping
    public String index(Model model, @RequestParam(required = false) String tenTheLoai,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
        if (page < 0) {
            page = 0;
        }
        Page<Genre> dsPage;
        if (StringUtils.hasText(tenTheLoai)) {
            dsPage = genreServices.findByName(tenTheLoai, PageRequest.of(page, size));
        } else {
            dsPage = genreServices.getAllWithPagination(PageRequest.of(page, size));
        }
        model.addAttribute("tenTheLoai", tenTheLoai);
        model.addAttribute("genres", dsPage);
        return "admin/genre/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("genre", new Genre());
        return "admin/genre/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("genre") Genre genre, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/genre/add";
        }
        genreServices.save(genre);
        return "redirect:/admin/theloai";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Genre genre = genreServices.getById(id);
        if (genre != null) {
            model.addAttribute("genre", genre);
            return "admin/genre/edit";
        }
        return "redirect:/admin/theloai";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id, @Valid @ModelAttribute("category") Genre genre, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/genre/edit";
        }
        genre.setGenreId(id);
        genreServices.save(genre);
        return "redirect:/admin/theloai";
    }
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        genreServices.delete(id);
        return "redirect:/admin/theloai";
    }


}
