package dayardiyev.shop.service;


import dayardiyev.shop.entity.User;
import dayardiyev.shop.entity.enumiration.Role;
import dayardiyev.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(){
        return userRepository.findById(13L).orElseThrow();
    }

    public boolean isAdminOrModer(User user){
        return user.getRole() == Role.MODERATOR || user.getRole() == Role.ADMIN;
    }

}
