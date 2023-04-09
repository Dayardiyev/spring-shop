package dayardiyev.shop.repository;

import dayardiyev.shop.entity.Order;
import dayardiyev.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserOrderById(User user);
}
