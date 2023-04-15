package dayardiyev.shop.controller;

import dayardiyev.shop.entity.Product;
import dayardiyev.shop.entity.Review;
import dayardiyev.shop.repository.ProductRepository;
import dayardiyev.shop.repository.ReviewRepository;
import dayardiyev.shop.repository.UserRepository;
import dayardiyev.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;


    @PostMapping(path = "/for_checking")
    public String publishReview(
            @RequestParam(name = "user_id") long userId,
            @RequestParam(name = "product_id") long productId,
            @RequestParam(name = "rating") int rating,
            @RequestParam(name = "review") String reviewText,
            RedirectAttributes ra
    ){
        Review review = new Review();
        review.setUser(userRepository.findById(userId).orElseThrow());
        review.setProduct(productRepository.findById(productId).orElseThrow());
        review.setPublished(false);
        review.setRating(rating);
        review.setText(reviewText);
        review.setCreated_at(LocalDateTime.now());
        reviewRepository.save(review);
        ra.addFlashAttribute("reviewMessage", "Отзыв успешно отправлен на проверку");
        return "redirect:/products/" + productId;
    }

    @GetMapping(path = "/review/delete/{id}")
    public String deleteReview(
            @PathVariable("id") long id){
        Review review = reviewRepository.findById(id).orElseThrow();
        reviewRepository.delete(review);
        Product product = review.getProduct();
        return "redirect:/products/" + product.getId();
    }

    @GetMapping("/user/reviews")
    public String userReviewListPage(Model model){
        model.addAttribute("reviews", reviewRepository.findAllByUserOrderByIdDesc(userService.getUser()));
        return "user_reviews";
    }

    @GetMapping(path = "/user/review/delete/{id}")
    public String userDeleteReview(
            @PathVariable("id") long id){
        Review review = reviewRepository.findById(id).orElseThrow();
        reviewRepository.delete(review);
        Product product = review.getProduct();
        return "redirect:/user/reviews";
    }

    @GetMapping("/admin/reviews")
    public String reviewModifyPage(Model model){
        if (userService.isAdminOrModer(userService.getUser())){
            model.addAttribute("reviews", reviewRepository.findAllNotPublishedReviews());
            return "admin_reviews";
        }
        return "redirect:/products";
    }

    @GetMapping("/admin/reviews/post")
    public String postReview(
            @RequestParam(name = "reviewId") long id
    ){
        Review review = reviewRepository.findById(id).orElseThrow();
        review.setPublished(true);
        reviewRepository.save(review);
        return "redirect:/admin/reviews";
    }
}

