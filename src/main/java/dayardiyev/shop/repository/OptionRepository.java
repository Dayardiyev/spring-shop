package dayardiyev.shop.repository;

import dayardiyev.shop.entity.Category;
import dayardiyev.shop.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByCategoryOrderById(Category category);
}
