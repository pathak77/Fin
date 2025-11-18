package com.fin.user.Service;


import com.fin.user.Dto.UserDto;

import java.net.http.HttpResponse;

public interface UserService {

    HttpResponse<String> addUser(UserDto userDto);
    HttpResponse<String> updateUser(UserDto userDto);
    HttpResponse<String> deleteUser(UserDto userDto);
    HttpResponse<String> getAllUsers();
    HttpResponse<String> getUserByUsername(String username);

}
