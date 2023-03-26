package dayardiyev.shop.controller;


import dayardiyev.shop.entity.Category;
import dayardiyev.shop.entity.Option;
import dayardiyev.shop.entity.Product;
import dayardiyev.shop.repository.CategoryRepository;
import dayardiyev.shop.repository.OptionRepository;
import dayardiyev.shop.repository.ProductRepository;
import dayardiyev.shop.repository.ValueRepository;
import dayardiyev.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private ProductService productService;

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

//        if (page == null) page = 0;
//        Pageable pageable = PageRequest.of(page, 10);
//        Page<Product> productPage = productRepository.findAll(pageable);

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
        model.addAttribute("categoryId", categoryId);

        return "products";
    }

    @GetMapping(path = "/products/{id}")
    public String getProduct(
            @PathVariable Long id,
            Model model
    ){
        Product product = productRepository.findById(id).orElseThrow();
        Category category = product.getCategory();
        List<Option> options = optionRepository.findAllByCategoryOrderById(category);
        model.addAttribute("product", product);
        model.addAttribute("options", options);
        return "product";
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
            model.addAttribute("options", optionRepository.findAllByCategoryOrderById(category));
            model.addAttribute("category", category);
        } else {
            model.addAttribute("categories", categoryRepository.findAll());
        }
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("product", new Product());
        return "add_product";
    }

    @PostMapping(path = "/products/add")
    public String saveCreatedProduct(
            @RequestParam long categoryId,
            @RequestParam List<String> optionValues,
            Product product) {
        productService.addProduct(categoryId, product, optionValues);
        return "redirect:/products";
    }

    @GetMapping(path = "/products/edit/{id}")
    public String updateProduct(
            @PathVariable("id") Long id,
            Model model) {
        Product product = productRepository.findById(id).orElseThrow();
        List<Option> options = optionRepository.findAllByCategoryOrderById(product.getCategory());
        model.addAttribute("product", product);
        model.addAttribute("options", options);
        return "update_product";
    }

    @PostMapping(path = "/products/edit/{id}")
    public String saveUpdatedProduct(
            @RequestParam long productId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer price,
            @RequestParam(required = false) List<String> updatedValues) {
        productService.updateProduct(productId, name, price, updatedValues);
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
