package EVC.Movie.repository;


import EVC.Movie.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICountryRepository extends JpaRepository<Country, Long> {

    Page<Country> findByNameContainingIgnoreCase(String tenCT, Pageable pageable);
}
