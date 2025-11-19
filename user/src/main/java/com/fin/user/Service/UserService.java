package com.fin.user.Service;

import com.fin.user.Dto.UserDto;
import com.fin.user.Entity.User;

import java.util.List;

public interface UserService<T> {

    String addUser(UserDto userDto);
    String updateUser(UserDto userDto);
    String deleteUser(UserDto userDto);
    List<T> getAllUsers();
    T getUserByUsername(String username);

}
