package dayardiyev.shop.controller;

import dayardiyev.shop.service.ReviewService;
import dayardiyev.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @PostMapping(path = "/for_checking")
    public String publishReview(
            @RequestParam(name = "product_id") long productId,
            @RequestParam(name = "rating") int rating,
            @RequestParam(name = "review") String reviewText,
            RedirectAttributes ra
    ){
        reviewService.createReview(productId, rating, reviewText);
        ra.addFlashAttribute("reviewMessage", "Отзыв успешно отправлен на проверку");
        return "redirect:/products/" + productId;
    }

    @GetMapping(path = "/review/delete/{id}")
    public String deleteReview(@PathVariable("id") long id){
        reviewService.deleteReview(id);
        return "redirect:/products/" + id;
    }

    @GetMapping("/user/reviews")
    public String userReviewListPage(Model model){
        model.addAttribute("reviews", reviewService.getAllByUser());
        return "user_reviews";
    }

    @GetMapping(path = "/user/review/delete/{id}")
    public String userDeleteReview(
            @PathVariable("id") long id){
        reviewService.deleteReview(id);
        return "redirect:/user/reviews";
    }

    @GetMapping("/admin/reviews")
    public String reviewModifyPage(Model model){
        if (userService.isAdminOrModer()){
            model.addAttribute("reviews", reviewService.getAllNotPublishedReviews());
            return "admin_reviews";
        }
        return "redirect:/products";
    }

    @GetMapping("/admin/reviews/post")
    public String postReview(
            @RequestParam(name = "reviewId") long id
    ){
        reviewService.postReview(id);
        return "redirect:/admin/reviews";
    }
}

