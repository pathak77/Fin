package com.fin.user.Service;

import com.fin.user.Dto.UserDto;
import com.fin.user.Entity.User;
import com.fin.user.UserRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Autowired
    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String addUser(UserDto userDto) {
        return null;
    }

    @Override
    public String updateUser(UserDto userDto) {
        return null;
    }

    @Override
    public String deleteUser(UserDto userDto) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }
}
