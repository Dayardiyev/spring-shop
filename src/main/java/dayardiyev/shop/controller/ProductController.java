package dayardiyev.shop.controller;


import dayardiyev.shop.entity.Category;
import dayardiyev.shop.entity.Option;
import dayardiyev.shop.entity.Product;
import dayardiyev.shop.entity.Review;
import dayardiyev.shop.service.CategoryService;
import dayardiyev.shop.service.OptionService;
import dayardiyev.shop.service.ProductService;
import dayardiyev.shop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping()
    public String getProducts(
            @RequestParam(required = false) Long categoryId,
            Model model
    ) {
        List<Product> products = productService.getProducts(categoryId);
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("categoryId", categoryId);
        return "products";
    }

    @GetMapping(path = "/{id}")
    public String getProduct(
            @PathVariable long id,
            Model model
    ) {
        Product product = productService.getSingleProduct(id);
        List<Option> options = optionService.getOptionsByCategory(product.getCategory());
        List<Review> reviews = reviewService.getAllPublishedReviews(product);

        model.addAttribute("product", product);
        model.addAttribute("options", options);
        model.addAttribute("reviews", reviews);
        return "product";
    }

    @GetMapping(path = "/add")
    public String createProduct(
            @RequestParam(required = false) Long categoryId,
            Model model) {
        if (categoryId != null) {
            Category category = categoryService.getById(categoryId);
            List<String> optionValues = new ArrayList<>();
            for (int i = 0; i < category.getOptions().size(); i++) {
                optionValues.add("");
            }
            model.addAttribute("optionValues", optionValues);
            model.addAttribute("options", optionService.getOptionsByCategory(category));
            model.addAttribute("category", category);
        } else {
            model.addAttribute("categories", categoryService.getCategories());
        }
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("product", new Product());
        return "product_add";
    }

    @PostMapping(path = "/add")
    public String saveCreatedProduct(
            @RequestParam long categoryId,
            @RequestParam List<String> optionValues,
            Product product,
            RedirectAttributes ra) {
        productService.addProduct(categoryId, product, optionValues);
        ra.addFlashAttribute("message", "Товар был успешно добавлен");
        return "redirect:/products";
    }

    @GetMapping(path = "/edit/{id}")
    public String updateProduct(
            @PathVariable("id") Long id,
            Model model) {
        Product product = productService.getSingleProduct(id);
        List<Option> options = optionService.getOptionsByCategory(product.getCategory());

        model.addAttribute("product", product);
        model.addAttribute("options", options);
        return "product_update";
    }

    @PostMapping(path = "/edit")
    public String saveUpdatedProduct(
            @RequestParam long productId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer price,
            @RequestParam(required = false) List<String> updatedValues,
            RedirectAttributes ra) {
        productService.updateProduct(productId, name, price, updatedValues);
        ra.addFlashAttribute("message", "Товар '" + name + "' был обновлен");
        return "redirect:/products";
    }

    @GetMapping(path = "/delete/{id}")
    public String deleteProduct(
            @PathVariable("id") Long id,
            RedirectAttributes ra
    ) {
        productService.deleteProduct(id);
        ra.addFlashAttribute("message", "Товар с id: " + id + " был удален");
        return "redirect:/products";
    }
}
