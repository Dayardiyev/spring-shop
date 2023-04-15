package dayardiyev.shop.repository;

import dayardiyev.shop.entity.Product;
import dayardiyev.shop.entity.Review;
import dayardiyev.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByProductOrderByIdDesc(Product product);

    List<Review> findAllByUserOrderByIdDesc(User user);

    @Query("select r from Review r where r.isPublished = true and r.product = ?1 order by r.id desc ")
    List<Review> findAllPublishedReviews(Product product);

    @Query("select r from Review r where r.isPublished = false order by r.id desc ")
    List<Review> findAllNotPublishedReviews();


    Review findByUserAndProduct(User user, Product product);
}
