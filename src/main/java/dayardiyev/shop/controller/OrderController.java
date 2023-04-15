package dayardiyev.shop.controller;


import dayardiyev.shop.entity.enumiration.Status;
import dayardiyev.shop.service.CartItemService;
import dayardiyev.shop.service.OrderService;
import dayardiyev.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/order")
    public String orderPage(Model model){
        model.addAttribute("cartItems", cartItemService.getItemsByUser());
        return "order";
    }

    @PostMapping(path = "/order")
    public String createOrder(
            @RequestParam String address
    ){
        orderService.createOrder(address);
        return "redirect:/user/orders";
    }

    @GetMapping("/user/orders")
    public String userOrderListPage(Model model){
        model.addAttribute("orders", orderService.getAllByUser());
        return "user_orders";
    }

    @GetMapping("/admin/orders")
    public String getOrdersAsAdminOrModer(Model model){
        if (userService.isAdminOrModer()){
            model.addAttribute("orders", orderService.getAll());
            return "admin_orders";
        }
        return "redirect:/products";
    }


    @GetMapping("/admin/orders/change_status")
    public String changeStatus(
            @RequestParam long orderId,
            @RequestParam Status status
    ){
        orderService.changeStatus(orderId, status);
        return "redirect:/admin/orders";
    }
}
