package dayardiyev.shop.service;


import dayardiyev.shop.entity.Product;
import dayardiyev.shop.entity.Review;
import dayardiyev.shop.entity.User;
import dayardiyev.shop.repository.ProductRepository;
import dayardiyev.shop.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class ReviewService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    public void createReview(long productId, int rating, String reviewText){
        Review review = new Review();
        review.setUser(userService.getUser());
        review.setProduct(productRepository.findById(productId).orElseThrow());
        review.setPublished(false);
        review.setRating(rating);
        review.setText(reviewText);
        review.setCreated_at(LocalDateTime.now());
        reviewRepository.save(review);
    }

    public void deleteReview(long id){
        Review review = reviewRepository.findById(id).orElseThrow();
        reviewRepository.delete(review);
    }

    public void postReview(long id){
        Review review = reviewRepository.findById(id).orElseThrow();
        review.setPublished(true);
        reviewRepository.save(review);
    }

    public boolean isReviewPresent(Product product, User user) {
        Review review = reviewRepository.findByUserAndProduct(user, product);
        return review != null;
    }

    public int getPublishedReviewsSize(Product product) {
        return reviewRepository.findAllPublishedReviews(product).size();
    }

    public String getReviewDate(LocalDateTime date) {
        return date.getDayOfMonth() + " "
                + getMonthOnRus(date) + " "
                + date.getYear();
    }

    public String getMonthOnRus(LocalDateTime date) {
        Month month = date.getMonth();
        Locale locale = new Locale("ru", "KZ");
        return month.getDisplayName(TextStyle.FULL, locale);
    }

    public List<Review> getAllPublishedReviews(Product product) {
        return reviewRepository.findAllPublishedReviews(product);
    }

    public List<Review> getAllByUser(){
        return reviewRepository.findAllByUserOrderByIdDesc(userService.getUser());
    }

    public List<Review> getAllNotPublishedReviews(){
        return reviewRepository.findAllNotPublishedReviews();
    }
}
