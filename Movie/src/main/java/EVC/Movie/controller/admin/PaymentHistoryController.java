package EVC.Movie.controller.admin;

import EVC.Movie.entity.PaymentHistory;
import EVC.Movie.services.PaymentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/paymentHistory")
public class PaymentHistoryController {
    @Autowired
    private PaymentHistoryService paymentHistoryService;

    @GetMapping("")
    public String paymentHistoryList(Model model, @RequestParam(required = false) String info,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "15") int size ) {
        if (page < 0) {
            page = 0;
        }
        Page<PaymentHistory> dsPage;
        if (StringUtils.hasText(info)){
            dsPage = paymentHistoryService.getByUserNameAndPaymentInfo(info, PageRequest.of(page, size));
        }
        else {
            dsPage = paymentHistoryService.getAllWithPagination(PageRequest.of(page, size));
        }
        model.addAttribute("info", info);
        model.addAttribute("paymentHistories", dsPage);
        return "admin/paymentHistory/list";
    }
}
