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

    public boolean isReviewPresent(Product product, User user){
        Review review = reviewRepository.findByUserAndProduct(user, product);
        return review == null;
    }

    public double getAvgRating(long productId){
        List<Review> reviews = productRepository.findById(productId).orElseThrow().getReviews();
        double avg = 0;
        if (!reviews.isEmpty()){
            for (Review review : reviews){
                avg = avg + review.getRating();
            }
            avg = avg / reviews.size();
        }
        return avg;
    }

    public String getMonthOnRus(LocalDateTime date){
        Month month = date.getMonth();
        Locale locale = new Locale("ru", "KZ");
        return month.getDisplayName(TextStyle.FULL, locale);
    }
}
