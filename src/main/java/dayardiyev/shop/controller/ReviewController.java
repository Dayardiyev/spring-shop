package dayardiyev.shop.controller;

import dayardiyev.shop.service.ReviewService;
import dayardiyev.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping(path = "/for_checking")
    public String publishReview(
            @RequestParam(name = "product_id") long productId,
            @RequestParam(name = "rating") int rating,
            @RequestParam(name = "review") String reviewText,
            RedirectAttributes ra
    ) {
        reviewService.createReview(productId, rating, reviewText);
        ra.addFlashAttribute("reviewMessage", "Отзыв успешно отправлен на проверку");
        return "redirect:/products/" + productId;
    }

    @GetMapping(path = "/review/delete/{id}")
    public String deleteReview(@PathVariable("id") long id) {
        reviewService.deleteReview(id);
        return "redirect:/products/" + id;
    }
}

