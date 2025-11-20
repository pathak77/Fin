package com.fin.user.Service;

import com.fin.user.Dto.UserDto;
import java.util.List;
import java.util.UUID;

public interface UserService<T> {

    String addUser(UserDto userDto);
    String updateUser(UUID id, UserDto userDto);
    String deleteUser(UserDto userDto);
    List<T> getAllUsers();
    T getUserByUsername(String username);

}
