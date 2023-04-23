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
import java.util.Calendar;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public User getUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Optional<User> user = userRepository.findByLogin(authentication.getName());
        return user.orElse(null);
    }

    public boolean isAuthenticated() {
        return getUser() != null;
    }

    public boolean isAdminOrModer() {
        if (isAuthenticated()) {
            return getUser().getRole() == Role.MODERATOR || getUser().getRole() == Role.ADMIN;
        }
        return false;
    }

    public String greeting() {
        return isAuthenticated() ? getMessageBasedOnHour() + ", " + getUser().getName() : getMessageBasedOnHour();
    }

    public String getMessageBasedOnHour() {
        Calendar c = Calendar.getInstance();
        int time = c.get(Calendar.HOUR_OF_DAY);
        if (time >= 6 && time <= 11) {
            return "Доброе утро";
        } else if (time >= 12 && time <= 16) {
            return "Добрый день";
        } else if (time >= 17 && time <= 21) {
            return "Добрый вечер";
        } else {
            return "Доброй ночи";
        }
    }

}
