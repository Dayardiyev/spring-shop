package dayardiyev.shop.repository;

import dayardiyev.shop.entity.CartItem;
import dayardiyev.shop.entity.Product;
import dayardiyev.shop.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByOrderById();

    List<CartItem> findAllByUserOrderById(User user);
    CartItem findByUserAndProduct(User user, Product product);

    @Transactional
    void deleteAllByUser(User user);
}
