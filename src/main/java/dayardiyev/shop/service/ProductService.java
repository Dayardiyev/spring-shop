package dayardiyev.shop.service;


import dayardiyev.shop.entity.*;
import dayardiyev.shop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private ReviewRepository reviewRepository;

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

    public void deleteProduct(long id){
        Product product = productRepository.findById(id).orElseThrow();
        valueRepository.deleteAll(product.getValues());
        reviewRepository.deleteAll(product.getReviews());
        productRepository.delete(product);
    }

    public String findValueByProductAndOption(Product product, Option option) {
        Value value = valueRepository.findByProductAndOption(product, option);
        return value != null ? value.getValue() : "";
    }

    public double getAvgRating(long productId){
        Product product = productRepository.findById(productId).orElseThrow();
        List<Review> reviews = reviewRepository.findAllPublishedReviews(product);
        double avg = 0;
        if (!reviews.isEmpty()){
            for (Review review : reviews){
                avg = avg + review.getRating();
            }
            avg = avg / reviews.size();
        }
        return avg;
    }

    public List<Product> getProducts(Long categoryId){
        Sort sort = Sort.by(
                Sort.Order.asc("category"),
                Sort.Order.asc("id")
        );
        List<Product> products =  productRepository.findAll(sort);
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElseThrow();
            products = category.getProducts();
        }
        return products;
    }

    public Product getSingleProduct(long id){
        return productRepository.findById(id).orElseThrow();
    }
}
