package dayardiyev.shop.service;


import dayardiyev.shop.entity.User;
import dayardiyev.shop.entity.enumiration.Role;
import dayardiyev.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveNewUser(User user){
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public User getUser(){
        return userRepository.findById(6L).orElseThrow();
    }

    public User getByLoginAndPassword(String login, String password){
        return userRepository.findByLoginAndPassword(login, password);
    }

    public boolean isAdminOrModer(){
        return getUser().getRole() == Role.MODERATOR || getUser().getRole() == Role.ADMIN;
    }

}
