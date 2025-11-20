package com.fin.user.Controller;

import com.fin.user.Dto.UserDto;
import com.fin.user.Entity.User;
import com.fin.user.Service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    UserServiceImpl userService;

    UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> allUserList = userService.getAllUsers();
        return new ResponseEntity<>(allUserList, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserDto userDto) {
        String message = userService.updateUser(userDto);
        return  new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addUser(@Valid @RequestBody UserDto userDto) {
        String message = userService.addUser(userDto);
        return  new ResponseEntity<>(message, HttpStatus.OK);
    }
}
