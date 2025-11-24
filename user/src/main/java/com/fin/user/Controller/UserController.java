package com.fin.user.Controller;

import com.fin.user.Dto.UserDto;
import com.fin.user.Entity.User;
import com.fin.user.Service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
        String message = userService.updateUser(userDto.getUserId(), userDto);
        return  new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addUser(@Valid @RequestBody UserDto userDto) {
        String message = userService.addUser(userDto);
        return  new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        String message =  userService.deleteUser(id);
        return  new ResponseEntity<>(message, HttpStatus.OK);
    }
}
