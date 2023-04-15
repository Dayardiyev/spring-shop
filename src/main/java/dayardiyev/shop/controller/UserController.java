package dayardiyev.shop.controller;


import dayardiyev.shop.entity.User;
import dayardiyev.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

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
        User user = userService.getByLoginAndPassword(login, password);
        if (user != null) {
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
        userService.saveNewUser(user);
        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/products";
    }
}
