package com.fin.user.Service;

import com.fin.user.Dto.UserDto;
import com.fin.user.Entity.User;
import com.fin.user.Mapper.UserMapper;
import com.fin.user.UserRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    final UserMapper userMapper;

    @Autowired
    UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    public String addUser(UserDto userDto) {
        User existingUser = userRepository.findByUsername(userDto.getUsername());

        if (existingUser != null) {
            return "User already exists" ;
        }

        User addedUser = userMapper.mapToUser(userDto);
        userRepository.save(addedUser);
        return "User " + addedUser.getUsername() + "added successfully";
    }

    @Override
    public String updateUser(UserDto userDto) {
        User existingUser = userRepository.findByUsername(userDto.getUsername());

        if (existingUser == null) {
            return "User does not exist" ;
        }

        User updatedUser = userMapper.mapToUser(userDto);
        userRepository.save(updatedUser);
        return "User " + updatedUser.getUsername() + "updated successfully";
    }

    @Override
    public String deleteUser(UserDto userDto) {
        User existingUser = userRepository.findByUsername(userDto.getUsername());

        if (existingUser == null) {
            return "User does not exist" ;
        }
        userRepository.delete(existingUser);
        return "User " + existingUser.getUsername() + "deleted successfully";
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User searchedUser = userRepository.findByUsername(username);
        if( searchedUser == null) {
            return null;
        }

        return userMapper.mapToUserDto(searchedUser);

    }
}
