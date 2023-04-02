package dayardiyev.shop.repository;

import dayardiyev.shop.entity.Product;
import dayardiyev.shop.entity.Review;
import dayardiyev.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByProductOrderByIdDesc(Product product);

    Review findByUserAndProduct(User user, Product product);
}
