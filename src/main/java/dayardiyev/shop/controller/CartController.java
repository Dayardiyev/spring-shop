package dayardiyev.shop.controller;

import dayardiyev.shop.entity.CartItem;
import dayardiyev.shop.entity.Product;
import dayardiyev.shop.entity.User;
import dayardiyev.shop.repository.CartItemRepository;
import dayardiyev.shop.repository.ProductRepository;
import dayardiyev.shop.repository.UserRepository;
import dayardiyev.shop.service.CartItemService;
import dayardiyev.shop.service.UserService;
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
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @GetMapping("/cart")
    public String getCartPage(Model model) {

        List<CartItem> cartItems = cartItemRepository.findAllByUserOrderById(userService.getUser());

        model.addAttribute("items", cartItems);
        model.addAttribute("totalPrice", cartItemService.getTotalPrice(cartItems));
        return "cart";
    }

    @GetMapping("/cart/remove")
    public String removeItemFromCart(
            @RequestParam long id
    ){
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow();
        cartItemRepository.delete(cartItem);
        return "redirect:/cart";
    }

    @GetMapping("/cart/remove_all")
    public String removeAllItems(){
        cartItemRepository.deleteAllByUser(userService.getUser());
        return "redirect:/cart";
    }

    @GetMapping("/increase_amount")
    public String increaseAmount(@RequestParam("cartItemId") Long id){
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow();
        cartItem.setAmount(cartItem.getAmount() + 1);
        cartItemRepository.save(cartItem);
        return "redirect:/cart";
    }

    @GetMapping("/decrease_amount")
    public String decreaseAmount(@RequestParam("cartItemId") Long id){
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow();
        if (cartItem.getAmount() > 1){
            cartItem.setAmount(cartItem.getAmount() - 1);
            cartItemRepository.save(cartItem);
        } else if (cartItem.getAmount() <= 1) {
            cartItemRepository.delete(cartItem);
        }
        return "redirect:/cart";
    }

    @PostMapping("/add_item")
    public String addProductToCart(
            @RequestParam Long productId,
            @RequestParam Long userId,
            RedirectAttributes redirectAttributes
    ) {
        Product product = productRepository.findById(productId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (cartItem != null) {
            cartItem.setAmount(cartItem.getAmount() + 1);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setUser(user);
            newCartItem.setAmount(1);
            cartItemRepository.save(newCartItem);
        }

        redirectAttributes.addFlashAttribute("message", "message");
        return "redirect:/products/" + product.getId();
    }
}
