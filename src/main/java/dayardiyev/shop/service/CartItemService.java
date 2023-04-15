package dayardiyev.shop.service;


import dayardiyev.shop.entity.CartItem;
import dayardiyev.shop.entity.Product;
import dayardiyev.shop.entity.User;
import dayardiyev.shop.repository.CartItemRepository;
import dayardiyev.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;


    public void addCartItem(long productId){
        Product product = productRepository.findById(productId).orElseThrow();
        CartItem cartItem = cartItemRepository.findByUserAndProduct(userService.getUser(), product);

        if (cartItem != null) {
            cartItem.setAmount(cartItem.getAmount() + 1);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setUser(userService.getUser());
            newCartItem.setAmount(1);
            cartItemRepository.save(newCartItem);
        }
    }

    public void removeItemFromCart(long id){
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow();
        cartItemRepository.delete(cartItem);
    }

    public void removeAllItems(){
        cartItemRepository.deleteAllByUser(userService.getUser());
    }

    public void increaseAmount(long id){
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow();
        cartItem.setAmount(cartItem.getAmount() + 1);
        cartItemRepository.save(cartItem);
    }

    public void decreaseAmount(long id){
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow();
        if (cartItem.getAmount() > 1){
            cartItem.setAmount(cartItem.getAmount() - 1);
            cartItemRepository.save(cartItem);
        } else if (cartItem.getAmount() <= 1) {
            cartItemRepository.delete(cartItem);
        }
    }

    public String cartAmount(){
        int amount = getAmountOfCartItems(userService.getUser());
        if (amount >= 2) return "(" + amount + " товара)";
        else if (amount == 1) return "(" + amount + " товар)";
        else return "";
    }

    public int getTotalPrice(List<CartItem> cartItems){
        int total = 0;
        for (CartItem cartItem : cartItems) {
            total = getPrice(cartItem);
        }
        return total;
    }

    public int getPrice(CartItem cartItem){
        return cartItem.getProduct().getPrice() * cartItem.getAmount();
    }

    public int getAmountOfCartItems(User user){
        List<CartItem> list = cartItemRepository.findAllByUserOrderById(user);
        int totalItems = 0;
        for (CartItem cartItem : list) {
            totalItems = totalItems + cartItem.getAmount();
        }
        return totalItems;
    }

    public List<CartItem> getItemsByUser(){
        return cartItemRepository.findAllByUserOrderById(userService.getUser());
    }
}
