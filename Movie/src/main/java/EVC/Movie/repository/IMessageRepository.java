package EVC.Movie.repository;


import EVC.Movie.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IMessageRepository extends JpaRepository<Message,Long> {
    @Query("SELECT m FROM Message m WHERE m.ngaydang >= :fromDate AND m.ngaydang <= :toDate")
    Page<Message> findAllWithCreationDateBetween(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);


    Page<Message> findAllByMovie_MovieIdOrderByNgaydangDesc(Long movieId,Pageable pageable);
    Page<Message> findAllByTypeOrderByNgaydangDesc(Boolean type,Pageable pageable);
    Page<Message> findAllByEmergencyOrderByNgaydangDesc(Boolean type,Pageable pageable);
    Page<Message> findAllByMovie_MovieIdAndTypeOrderByNgaydangDesc(Long movieId,Boolean type,Pageable pageable);
    Page<Message> findAllByMovie_MovieIdAndEmergencyOrderByNgaydangDesc(Long movieId,Boolean type,Pageable pageable);
    Page<Message> findAllByTypeAndEmergencyOrderByNgaydangDesc(Boolean type,Boolean emg,Pageable pageable);
    Page<Message> findAllByMovie_MovieIdAndTypeAndEmergencyOrderByNgaydangDesc(Long movieId,Boolean type,Boolean emg,Pageable pageable);

    Page<Message> findAllByOrderByNgaydangDesc(Pageable pageable);

    List<Message> findAllByEmergencyAndTypeOrderByNgaydangDesc(Boolean emg,Boolean type);

}
