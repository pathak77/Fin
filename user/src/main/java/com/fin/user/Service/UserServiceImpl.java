package com.fin.user.Service;

import com.fin.user.Dto.UserDto;
import com.fin.user.UserRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Autowired
    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public HttpResponse<String> addUser(UserDto userDto) {
        return null;
    }

    @Override
    public HttpResponse<String> updateUser(UserDto userDto) {
        return null;
    }

    @Override
    public HttpResponse<String> deleteUser(UserDto userDto) {
        return null;
    }

    @Override
    public HttpResponse<String> getAllUsers() {
        return null;
    }

    @Override
    public HttpResponse<String> getUserByUsername(String username) {
        return null;
    }
}
