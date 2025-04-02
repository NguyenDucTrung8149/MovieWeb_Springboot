package EVC.Movie.services;

import EVC.Movie.entity.PaymentHistory;
import EVC.Movie.repository.IPaymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentHistoryService {
    @Autowired
    private IPaymentHistoryRepository paymentHistoryRepository;
    public Page<PaymentHistory> getByUserNameAndPaymentInfo(String info, Pageable pageable){
        return paymentHistoryRepository.findByUserUserNameOrPaymentInfoContainingIgnoreCase(info, info, pageable);
    }

    public Page<PaymentHistory> getAllWithPagination(Pageable pageable){
        return paymentHistoryRepository.findAll(pageable);
    }

    public PaymentHistory save(PaymentHistory paymentHistory){
        return paymentHistoryRepository.save(paymentHistory);
    }
}
