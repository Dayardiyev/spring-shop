package dayardiyev.shop.service;


import dayardiyev.shop.entity.User;
import dayardiyev.shop.entity.enumiration.Role;
import dayardiyev.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveNewUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public User getUser(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return userRepository.findByLogin(authentication.getName()).orElseThrow();
    }

    public boolean isAdminOrModer(){
        return getUser().getRole() == Role.MODERATOR || getUser().getRole() == Role.ADMIN;
    }

    public String greeting(){
        Random generator = new Random();
        String[] greetings = {"Здравствуй", "Приветствую", "Добро пожаловать", "Салам", "Привет", "Бонжур"};
        int randomIndex = generator.nextInt(greetings.length);
        return greetings[randomIndex] + ", ";
    }

}
