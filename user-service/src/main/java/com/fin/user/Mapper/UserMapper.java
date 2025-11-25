package com.fin.user.Mapper;


import com.fin.user.Dto.UserDto;
import com.fin.user.Entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .username((user.getUsername() == null) ?"":user.getUsername())
                .password(user.getPassword())
                .build();
    }

    public User mapToUser(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();
    }

    public List<UserDto> mapToUserDtoList(List<User> users) {
        List<UserDto> userDtoList = new ArrayList<>();

        for(User user : users) {
            UserDto userDto = mapToUserDto(user);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    public User updateUserFromDto(UserDto userDto, User existingUser) {
        return User.builder()
                .email( (userDto.getEmail()).isBlank() ? existingUser.getEmail():userDto.getEmail())
                .username((userDto.getUsername()).isBlank() ? existingUser.getUsername():userDto.getUsername())
                .password(userDto.getPassword())
                .build();
    }
}
