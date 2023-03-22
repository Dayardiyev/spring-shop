package dayardiyev.shop.repository;

import dayardiyev.shop.entity.Option;
import dayardiyev.shop.entity.Product;
import dayardiyev.shop.entity.Value;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValueRepository extends JpaRepository<Value, Long> {
    Value findByProductAndOption(Product product, Option option);

}
