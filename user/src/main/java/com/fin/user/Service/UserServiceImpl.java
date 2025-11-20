package com.fin.user.Service;

import com.fin.user.Dto.UserDto;
import com.fin.user.Entity.User;
import com.fin.user.GlobalExceptions.EmailExistsException;
import com.fin.user.GlobalExceptions.UserAlreadyExistException;
import com.fin.user.GlobalExceptions.UserExceptions;
import com.fin.user.GlobalExceptions.UserNotFoundException;
import com.fin.user.Mapper.UserMapper;
import com.fin.user.UserRepository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    @Transactional
    public String addUser(UserDto userDto) {
        User existingUser = userRepository.findByUsername(userDto.getUsername());

        if (existingUser.getUsername() != null) {
            throw new UserAlreadyExistException("User with username" + existingUser.getUsername() + "already exists");
        }
        else if(userDto.getEmail() != null) {
            throw new EmailExistsException("User with email" + existingUser.getEmail() + "already exists");
        }

        User addedUser = userMapper.mapToUser(userDto);
        userRepository.save(addedUser);
        return "User " + addedUser.getUsername() + "added successfully";
    }

    @Override
    @Transactional
    public String updateUser(UUID id, UserDto userDto) {
        Optional<User> existingUser = userRepository.findById(id);

        if (!existingUser.isPresent()) {
            throw new UserNotFoundException("User not found");
        }


        User updatedUser = userMapper.mapToUser(userDto);
        userRepository.save(updatedUser);
        return "User " + updatedUser.getUsername() + "updated successfully";
    }

    @Override
    @Transactional
    public String deleteUser(UserDto userDto) {
        User existingUser = userRepository.findByUsername(userDto.getUsername());

        if (existingUser == null) {
            throw new UserNotFoundException("User with username" + userDto.getUsername() + "does not exist");
        }
        userRepository.delete(existingUser);
        return "User " + existingUser.getUsername() + "deleted successfully";
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public UserDto getUserByUsername(String username) {
        User searchedUser = userRepository.findByUsername(username);
        if( searchedUser == null) {
            return null;
        }

        return userMapper.mapToUserDto(searchedUser);

    }
}
