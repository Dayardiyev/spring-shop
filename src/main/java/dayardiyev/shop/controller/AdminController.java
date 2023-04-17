package dayardiyev.shop.controller;

import dayardiyev.shop.entity.enumiration.Status;
import dayardiyev.shop.service.OrderService;
import dayardiyev.shop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private OrderService orderService;

    @GetMapping(path = "/reviews")
    public String reviewModifyPage(Model model) {
        model.addAttribute("reviews", reviewService.getNotPublishedReviews());
        return "admin_reviews";
    }

    @GetMapping(path = "/reviews/post")
    public String postReview(
            @RequestParam(name = "reviewId") long id
    ) {
        reviewService.postReview(id);
        return "redirect:/admin/reviews";
    }

    @GetMapping(path = "/orders")
    public String getOrdersAsAdminOrModer(Model model) {
        model.addAttribute("orders", orderService.getOrders());
        return "admin_orders";
    }

    @GetMapping(path = "/orders/change_status")
    public String changeStatus(
            @RequestParam long orderId,
            @RequestParam Status status
    ) {
        orderService.changeStatus(orderId, status);
        return "redirect:/admin/orders";
    }
}
