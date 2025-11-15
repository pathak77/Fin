package com.fin.user.Dto;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserDto {

    UUID userId;
    String name;
    String email;

}
