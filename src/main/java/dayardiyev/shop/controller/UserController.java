package dayardiyev.shop.controller;


import dayardiyev.shop.entity.User;
import dayardiyev.shop.service.OrderService;
import dayardiyev.shop.service.ReviewService;
import dayardiyev.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private OrderService orderService;

    @GetMapping(path = "/registration")
    public String userSignUp(Model model) {
        model.addAttribute("user", new User());
        return "user_registration";
    }

    @PostMapping(path = "/registration")
    public String saveNewUser(User user) {
        userService.saveNewUser(user);
        return "redirect:/products";
    }

    @GetMapping(path = "/user")
    public String userInfoPage(Model model) {
        model.addAttribute("user", userService.getUser());
        return "user_information";
    }

    @GetMapping(path = "/user/reviews")
    public String userReviewListPage(Model model) {
        model.addAttribute("reviews", reviewService.getReviewsByUser());
        return "user_reviews";
    }

    @GetMapping(path = "/user/review/delete/{id}")
    public String userDeleteReview(
            @PathVariable("id") long id) {
        reviewService.deleteReview(id);
        return "redirect:/user/reviews";
    }

    @GetMapping(path = "/user/orders")
    public String userOrderListPage(Model model) {
        model.addAttribute("orders", orderService.getOrdersByUser());
        return "user_orders";
    }
}
