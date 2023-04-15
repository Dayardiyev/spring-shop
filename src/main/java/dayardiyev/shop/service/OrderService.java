package dayardiyev.shop.service;

import dayardiyev.shop.entity.CartItem;
import dayardiyev.shop.entity.Order;
import dayardiyev.shop.entity.OrderProduct;
import dayardiyev.shop.entity.enumiration.Status;
import dayardiyev.shop.repository.CartItemRepository;
import dayardiyev.shop.repository.OrderProductRepository;
import dayardiyev.shop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class OrderService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private UserService userService;

    public void createOrder(String address){
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
    }

    public void changeStatus(long id, Status status){
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);
    }

    public List<Order> getAllByUser(){
        return orderRepository.findAllByUserOrderByIdDesc(userService.getUser());
    }

    public List<Order> getAll(){
        return orderRepository.findAllByOrderById();
    }

    public List<Status> getStatuses(){
        return List.of(Status.values());
    }

    public String getOrderDate(LocalDateTime date){
        return date.getDayOfMonth() + " "
                + getMonthOnRus(date) + " "
                + date.getYear() + " г. в "
                + date.getHour() + ":"
                + String.format("%02d", date.getMinute());
    }

    public String getMonthOnRus(LocalDateTime date){
        Month month = date.getMonth();
        Locale locale = new Locale("ru", "KZ");
        return month.getDisplayName(TextStyle.SHORT, locale);
    }
}
