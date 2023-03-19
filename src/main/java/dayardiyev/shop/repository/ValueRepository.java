package dayardiyev.shop.repository;

import dayardiyev.shop.entity.Option;
import dayardiyev.shop.entity.Product;
import dayardiyev.shop.entity.Value;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValueRepository extends JpaRepository<Value, Long> {
    Value findValueByProductAndOption(Product product, Option option);

    List<Value> findAllByProductOrderByOption(Product product);

}
