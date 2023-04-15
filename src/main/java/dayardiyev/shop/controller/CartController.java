package dayardiyev.shop.controller;

import dayardiyev.shop.entity.CartItem;
import dayardiyev.shop.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/cart")
    public String getCartPage(Model model) {
        List<CartItem> cartItems = cartItemService.getItemsByUser();

        model.addAttribute("items", cartItems);
        model.addAttribute("totalPrice", cartItemService.getTotalPrice(cartItems));
        return "cart";
    }

    @GetMapping("/cart/remove")
    public String removeItemFromCart(@RequestParam long id){
        cartItemService.removeItemFromCart(id);
        return "redirect:/cart";
    }

    @GetMapping("/cart/remove_all")
    public String removeAllItems(){
        cartItemService.removeAllItems();
        return "redirect:/cart";
    }

    @GetMapping("/increase_amount")
    public String increaseAmount(@RequestParam("cartItemId") Long id){
        cartItemService.increaseAmount(id);
        return "redirect:/cart";
    }

    @GetMapping("/decrease_amount")
    public String decreaseAmount(@RequestParam("cartItemId") Long id){
        cartItemService.decreaseAmount(id);
        return "redirect:/cart";
    }

    @PostMapping("/add_item")
    public String addProductToCart(
            @RequestParam long productId,
            RedirectAttributes redirectAttributes
    ) {
        cartItemService.addCartItem(productId);
        redirectAttributes.addFlashAttribute("message", "message");
        return "redirect:/products/" + productId;
    }
}
