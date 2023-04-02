package dayardiyev.shop.service;


import dayardiyev.shop.entity.User;
import dayardiyev.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(){
        return userRepository.findById(14L).orElseThrow();
    }

}
