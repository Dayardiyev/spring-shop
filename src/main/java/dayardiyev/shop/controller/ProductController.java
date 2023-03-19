package dayardiyev.shop.controller;


import dayardiyev.shop.entity.Category;
import dayardiyev.shop.entity.Option;
import dayardiyev.shop.entity.Product;
import dayardiyev.shop.entity.Value;
import dayardiyev.shop.repository.CategoryRepository;
import dayardiyev.shop.repository.OptionRepository;
import dayardiyev.shop.repository.ProductRepository;
import dayardiyev.shop.repository.ValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ValueRepository valueRepository;

    @Autowired
    private OptionRepository optionRepository;


    @GetMapping(path = "/products")
    public String getProducts(
            @RequestParam(required = false) Long categoryId,
            Model model
    ) {
        Sort sort = Sort.by(
                Sort.Order.asc("category"),
                Sort.Order.asc("id")
        );
        List<Product> products = productRepository.findAll(sort);
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElseThrow();
            model.addAttribute("category_name", category.getName());
            products = category.getProducts();
        }

        int avg = 0;
        if (!products.isEmpty()) {
            for (Product product : products) {
                avg = avg + product.getPrice();
            }
            avg = avg / products.size();
        }

        model.addAttribute("avg", avg);
        model.addAttribute("products", products);
        model.addAttribute("category_id", categoryId);

        return "products";
    }

    @GetMapping(path = "/products/add")
    public String createProduct(
            @RequestParam(required = false) Long categoryId,
            Model model) {
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElseThrow();
            List<String> optionValues = new ArrayList<>();
            for (int i = 0; i < category.getOptions().size(); i++) {
                optionValues.add("");
            }
            model.addAttribute("optionValues", optionValues);
            model.addAttribute("category", category);
        } else {
            model.addAttribute("categories", categoryRepository.findAll());
        }
        model.addAttribute("product", new Product());
        return "add_product";
    }

    @PostMapping(path = "/products/add")
    public String saveCreatedProduct(
            @RequestParam long categoryId,
            @RequestParam List<String> optionValues,
            Product product) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        product.setCategory(category);
        productRepository.save(product);

        for (int i = 0; i < optionValues.size(); i++) {
            Value value = new Value();
            value.setProduct(product);
            value.setOption(category.getOptions().get(i));
            value.setValue(optionValues.get(i));
            valueRepository.save(value);
        }

        return "redirect:/products";
    }

    @GetMapping(path = "/products/edit/{id}")
    public String updateProduct(
            @PathVariable("id") Long id,
            Model model) {
        Product product = productRepository.findById(id).orElseThrow();

        List<Option> options = optionRepository.findAllByCategoryOrderById(product.getCategory());
        List<Value> values = valueRepository.findAllByProductOrderByOption(product);
        if (values.size() < options.size()) {
            for (Option option : options) {
                Value valueByProductAndOption = valueRepository
                        .findValueByProductAndOption(product, option);
                if (valueByProductAndOption == null) {
                    Value value = new Value();
                    value.setProduct(product);
                    value.setOption(option);
                    value.setValue("NULL");
                    valueRepository.saveAndFlush(value);
                }
            }
        }
        model.addAttribute("product", product);
        model.addAttribute("values", values);
        model.addAttribute("options", options);
        return "update_product";
    }

    @PostMapping(path = "/products/edit/{id}")
    public String saveUpdatedProduct(
            @RequestParam long id,
            @RequestParam(required = false) String updatedName,
            @RequestParam(required = false) Integer updatedPrice,
            @RequestParam(required = false) List<String> updatedValues) {
        Product product = productRepository.findById(id).orElseThrow();
        if (updatedName != null) product.setName(updatedName);
        if (updatedPrice != null) product.setPrice(updatedPrice);
        for (int i = 0; i < updatedValues.size(); i++) {
            Value value = product.getValues().get(i);
            value.setValue(updatedValues.get(i));
            valueRepository.save(value);
        }
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping(path = "/products/delete/{id}")
    public String deleteProduct(
            @PathVariable("id") Long id
    ) {
        Product product = productRepository.findById(id).orElseThrow();

        valueRepository.deleteAll(product.getValues());

        productRepository.delete(product);

        return "redirect:/products";
    }

}
