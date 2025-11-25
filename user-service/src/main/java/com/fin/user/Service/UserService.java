package com.fin.user.Service;

import com.fin.user.Dto.UserDto;
import java.util.List;
import java.util.UUID;

public interface UserService<T> {

    UserDto addUser(UserDto userDto);
    UserDto updateUser(UUID id, UserDto userDto);
    Boolean deleteUser(UUID userId);
    List<T> getAllUsers();
    T getUserByUsername(String username);

}
