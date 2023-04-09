package dayardiyev.shop.service;


import dayardiyev.shop.entity.CartItem;
import dayardiyev.shop.entity.User;
import dayardiyev.shop.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    public String cartAmount(){
        int amount = getAmountOfCartItems(userService.getUser());
        if (amount > 1) return "(" + amount + " товара)";
        else if (amount == 1) return "(" + amount + " товар)";
        else return "";
    }

    public int getTotalPrice(List<CartItem> cartItems){
        int total = 0;
        for (CartItem cartItem : cartItems) {
            for (int i = 0; i < cartItem.getAmount(); i++) {
                total += cartItem.getProduct().getPrice();
            }
        }
        return total;
    }

    public int getAmountOfCartItems(User user){
        List<CartItem> list = cartItemRepository.findAllByUserOrderById(user);
        int totalItems = 0;
        for (CartItem cartItem : list) {
            totalItems = totalItems + cartItem.getAmount();
        }
        return totalItems;
    }
}
