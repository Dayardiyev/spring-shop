package dayardiyev.shop.repository;

import dayardiyev.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByLoginAndPassword(String login, String password);
}
