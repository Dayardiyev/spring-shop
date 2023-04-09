package dayardiyev.shop.service;


import dayardiyev.shop.entity.Category;
import dayardiyev.shop.entity.Option;
import dayardiyev.shop.entity.Product;
import dayardiyev.shop.entity.Value;
import dayardiyev.shop.repository.CategoryRepository;
import dayardiyev.shop.repository.OptionRepository;
import dayardiyev.shop.repository.ProductRepository;
import dayardiyev.shop.repository.ValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class ProductService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ValueRepository valueRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    public void addProduct(long categoryId, Product product, List<String> values) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        List<Option> options = optionRepository.findAllByCategoryOrderById(category);
        product.setCategory(category);
        productRepository.save(product);

        for (int i = 0; i < options.size(); i++) {
            Value value = new Value();
            value.setProduct(product);
            value.setOption(options.get(i));
            value.setValue(values.get(i));
            valueRepository.save(value);
        }
    }

    public void updateProduct(long productId, String name, Integer price, List<String> values) {
        Product product = productRepository.findById(productId).orElseThrow();
        if (name != null) product.setName(name);
        if (price != null) product.setPrice(price);
        productRepository.save(product);

        List<Option> options = optionRepository.findAllByCategoryOrderById(product.getCategory());
        for (int i = 0; i < options.size(); i++) {
            Value value = valueRepository.findByProductAndOption(product, options.get(i));
            if (value == null) {
                value = new Value();
                value.setProduct(product);
                value.setOption(options.get(i));
                value.setValue(values.get(i));
            } else if (!value.getValue().equals(values.get(i))) {
                value.setValue(values.get(i));
            }
            valueRepository.save(value);
        }
    }

    public String findValueByProductAndOption(Product product, Option option) {
        Value value = valueRepository.findByProductAndOption(product, option);
        return value != null ? value.getValue() : "";
    }

    public String getFirstOption(Product product){
        List<Option> options = optionRepository.findAllByCategoryOrderById(product.getCategory());
        return options.get(0).getName();
    }
}
