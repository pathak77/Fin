package com.fin.user.Controller;

import com.fin.user.Entity.User;
import com.fin.user.Service.UserServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.List;

@RestController("/user")
public class UserController {

    UserServiceImpl userService;

    UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public HttpResponse<List<User>> getAll() {
        List<User> allUserList = userService.getAllUsers();
    }
}
