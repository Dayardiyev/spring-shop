package dayardiyev.shop.controller;


import dayardiyev.shop.entity.CartItem;
import dayardiyev.shop.entity.Order;
import dayardiyev.shop.entity.OrderProduct;
import dayardiyev.shop.entity.enumiration.Status;
import dayardiyev.shop.repository.CartItemRepository;
import dayardiyev.shop.repository.OrderProductRepository;
import dayardiyev.shop.repository.OrderRepository;
import dayardiyev.shop.service.CartItemService;
import dayardiyev.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/order")
    public String orderPage(Model model){
        model.addAttribute("cartItems", cartItemRepository.findAllByUserOrderById(userService.getUser()));
        return "order";
    }

    @PostMapping(path = "/order")
    public String addOrder(
            @RequestParam String address
    ){
        List<CartItem> cartItems = cartItemRepository.findAllByUserOrderById(userService.getUser());
        Order order = new Order();
        order.setUser(userService.getUser());
        order.setAddress(address);
        order.setStatus(Status.INSTOCK);
        order.setCreated_at(LocalDateTime.now());
        orderRepository.save(order);
        for (CartItem cartItem : cartItems) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(cartItem.getProduct());
            orderProduct.setAmount(cartItem.getAmount());
            orderProductRepository.save(orderProduct);
        }
        cartItemRepository.deleteAllByUser(userService.getUser());
        return "redirect:/user/orders";
    }

    @GetMapping("/admin/orders")
    public String getOrdersAsAdminOrModer(Model model){
        if (userService.isAdminOrModer(userService.getUser())){
            model.addAttribute("orders", orderRepository.findAllByOrderById());
            return "admin_orders";
        }
        return "redirect:/products";
    }


    @GetMapping("/admin/orders/change_status")
    public String changeStatus(
            @RequestParam long orderId,
            @RequestParam Status status
    ){
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);
        return "redirect:/admin/orders";
    }
}
