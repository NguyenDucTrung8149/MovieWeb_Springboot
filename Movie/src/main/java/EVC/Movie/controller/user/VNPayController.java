package EVC.Movie.controller.user;

import EVC.Movie.entity.PaymentHistory;
import EVC.Movie.entity.User;
import EVC.Movie.services.PaymentHistoryService;
import EVC.Movie.services.UserServices;
import EVC.Movie.services.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/vnpay")
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private UserServices userServices;

    @Autowired
    private PaymentHistoryService paymentHistoryService;


    @GetMapping("/submitOrder")
    public String submitOrder(
            @RequestParam("planDuration") int planDuration,
            @RequestParam("userid") Long userid,
            HttpServletRequest request) {

        String orderInfo = "Goi VIP " + planDuration + " thang";
        int orderTotal = planDuration == 1 ? 109000 : (planDuration == 3 ? 249000 : 990000);
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl + "/vnpay");

        request.getSession().setAttribute("userid", userid);
        request.getSession().setAttribute("planDuration", planDuration);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        HttpSession session = request.getSession();
        Long userid = (Long) session.getAttribute("userid");
        Integer planDuration = (Integer) session.getAttribute("planDuration");

        if (paymentStatus == 1) {
            User currentUser = userServices.findById(userid);
            LocalDateTime now = LocalDateTime.now();
            if (currentUser.getPremiumDuration() == null || currentUser.getPremiumDuration().isBefore(now)) {
                currentUser.setPremiumDuration(now.plus(planDuration, ChronoUnit.MONTHS));
            } else {
                currentUser.setPremiumDuration(currentUser.getPremiumDuration().plus(planDuration, ChronoUnit.MONTHS));
            }
            userServices.update(currentUser);

            PaymentHistory paymentHistory = new PaymentHistory();
            paymentHistory.setUser(currentUser);
            paymentHistory.setMoney(Double.parseDouble(totalPrice)/100);
            paymentHistory.setPaymentDate(now);
            paymentHistory.setPaymentInfo(orderInfo);
            paymentHistoryService.save(paymentHistory);

            return "user/vnpay/ordersuccess";
        } else
        {
            return "user/vnpay/orderfail";
        }
    }
}
