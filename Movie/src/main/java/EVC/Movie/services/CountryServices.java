package EVC.Movie.services;


import EVC.Movie.entity.Country;
import EVC.Movie.repository.ICountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServices {
    @Autowired
    private ICountryRepository countryRepository;

    public List<Country> getAll () {
        return countryRepository.findAll();
    }

    public Country getById(Long id) {
        return countryRepository.findById(id).orElse(null);
    }

    public Country save(Country category) {
        return countryRepository.save(category);
    }

    public void delete(Long id) {
        countryRepository.deleteById(id);
    }
    public Page<Country> findByName(String tenCountry, Pageable pageable) {
        return countryRepository.findByNameContainingIgnoreCase(tenCountry, pageable);
    }
    public Page<Country> getAllWithPagination(Pageable pageable) {
        return countryRepository.findAll(pageable);
    }
}
