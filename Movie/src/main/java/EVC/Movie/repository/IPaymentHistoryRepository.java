package EVC.Movie.repository;

import EVC.Movie.entity.PaymentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IPaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    Page<PaymentHistory> findByUserUserNameOrPaymentInfoContainingIgnoreCase(String info, String info2, Pageable pageable);
}
