package com.fin.user.Service;

import com.fin.user.Dto.UserDto;
import java.util.List;
import java.util.UUID;

public interface UserService<T> {

    UserDto addUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    Boolean deleteUser(Long userId);
    List<T> getAllUsers();
    T getUserByUsername(String username);

}
