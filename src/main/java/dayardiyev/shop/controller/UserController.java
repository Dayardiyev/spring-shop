package dayardiyev.shop.controller;


import dayardiyev.shop.entity.User;
import dayardiyev.shop.entity.enumiration.Role;
import dayardiyev.shop.repository.OrderProductRepository;
import dayardiyev.shop.repository.OrderRepository;
import dayardiyev.shop.repository.ReviewRepository;
import dayardiyev.shop.repository.UserRepository;
import dayardiyev.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping(path = "/")
    public String showUserSignIn() {
        return "user_login";
    }

    @PostMapping(path = "/login")
    public String userSignIn(
            @RequestParam String login,
            @RequestParam String password,
            RedirectAttributes redirectAttributes,
            Model model) {
        User user = userRepository.findByLoginAndPassword(login, password);
        if (user != null) {
//            model.addAttribute("user", user);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/products";
        } else {
            model.addAttribute("error", "Неверный логин или пароль");
            return "user_login";
        }
    }

    @GetMapping(path = "/registration")
    public String userSignUp(Model model) {
        model.addAttribute("user", new User());
        return "user_registration";
    }

    @PostMapping(path = "/registration")
    public String saveNewUser(User user, RedirectAttributes redirectAttributes) {
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Role.USER);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/products";
    }

    @GetMapping(path = "/user")
    public String userProfile(@RequestParam(name = "id") long userId, Model model) {
        User user = userRepository.findById(userId).orElseThrow();
        model.addAttribute("user", user);
        return "user_information";
    }
}
