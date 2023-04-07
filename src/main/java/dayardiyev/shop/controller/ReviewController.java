package dayardiyev.shop.controller;

import dayardiyev.shop.entity.Product;
import dayardiyev.shop.entity.Review;
import dayardiyev.shop.repository.ProductRepository;
import dayardiyev.shop.repository.ReviewRepository;
import dayardiyev.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    @PostMapping(path = "/publish_review")
    public String publishReview(
            @RequestParam(name = "user_id") long userId,
            @RequestParam(name = "product_id") long productId,
            @RequestParam(name = "rating") int rating,
            @RequestParam(name = "review") String reviewText
    ){
        Review review = new Review();
        review.setUser(userRepository.findById(userId).orElseThrow());
        review.setProduct(productRepository.findById(productId).orElseThrow());
        review.setRating(rating);
        review.setText(reviewText);
        review.setCreated_at(LocalDateTime.now());
        reviewRepository.save(review);
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
}

