package dayardiyev.shop.controller;


import dayardiyev.shop.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/user_controller")
public class UsersController {

    private static final User[] USERS = new User[]{
            new User("Bob", 42),
            new User("Kenny", 38),
            new User("Steve", 35),
            new User("John", 20),
            new User("Kevin", 18),
            new User("Peter", 15)
    };

    @GetMapping(path = "/users")
    public List<User> getUsers(
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer to
    ) {
        List<User> userList = new ArrayList<>();

        if (from == null) from = Integer.MIN_VALUE;
        if (to == null) to = Integer.MAX_VALUE;
        for (User user : USERS) {
            if (user.getAge() >= from && user.getAge() <= to) userList.add(user);
        }
        return userList;
    }

}
