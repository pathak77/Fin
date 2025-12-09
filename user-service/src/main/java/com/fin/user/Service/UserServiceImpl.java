package com.fin.user.Service;

import com.fin.user.Dto.UserDto;
import com.fin.user.Entity.User;
import com.fin.user.GlobalExceptions.EmailExistsException;
import com.fin.user.GlobalExceptions.UserAlreadyExistException;
import com.fin.user.GlobalExceptions.UserNotFoundException;
import com.fin.user.Mapper.UserMapper;
import com.fin.user.UserRepository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistException("Username '" + userDto.getUsername() + "' is already taken");
        }

        if (userDto.getEmail() != null && userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailExistsException("Email '" + userDto.getEmail() + "' is already registered");
        }

        User user = userMapper.mapToUser(userDto);
        User savedUser = userRepository.save(user);

        return userMapper.mapToUserDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));


        if (!existingUser.getUsername().equals(userDto.getUsername()) &&
                userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistException("Username '" + userDto.getUsername() + "' is already taken");
        }

        if (userDto.getEmail() != null &&
                !userDto.getEmail().equals(existingUser.getEmail()) &&
                userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailExistsException("Email '" + userDto.getEmail() + "' is already in use");
        }

        User updatedUserFromDto = userMapper.updateUserFromDto(userDto, existingUser);

        User updatedUser = userRepository.save(updatedUserFromDto);
        return userMapper.mapToUserDto(updatedUserFromDto);
    }

    @Override
    @Transactional
    public Boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public UserDto getUserByUsername(String username) {

        if(!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException("User not found with username: " + username);
        }
        User searchedUser = userRepository.findByUsername(username);
        return userMapper.mapToUserDto(searchedUser);

    }
}
