package dayardiyev.shop.controller;


import dayardiyev.shop.entity.Category;
import dayardiyev.shop.entity.Product;
import dayardiyev.shop.repository.CategoryRepository;
import dayardiyev.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;


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

    @GetMapping(path = "/products/new")
    public String createProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "create_product";
    }

    @PostMapping(path = "/products/new")
    public String saveCreatedProduct(
            @RequestParam long categoryId,
            Product product){
        product.setCategory(categoryRepository.findById(categoryId).orElseThrow());
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping(path = "/products/edit/{id}")
    public String updateProduct(
            @PathVariable("id") Long id,
            Model model) {
        Product product = productRepository.findById(id).orElseThrow();
        System.out.println(product.getId());
        model.addAttribute("product", product);
        return "update_product";
    }

    @PostMapping(path = "/products/edit/{id}")
    public String saveUpdatedProduct(
            @RequestParam long id,
            @RequestParam(required = false) String updatedName,
            @RequestParam(required = false) Integer updatedPrice) {
        Product product = productRepository.findById(id).orElseThrow();
        if (updatedName != null) product.setName(updatedName);
        if (updatedPrice != null) product.setPrice(updatedPrice);
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping(path = "/products/delete/{id}")
    public String deleteProduct(
            @PathVariable("id") Long id
    ){
        productRepository.delete(productRepository.findById(id).orElseThrow());
        return "redirect:/products";
    }

}
