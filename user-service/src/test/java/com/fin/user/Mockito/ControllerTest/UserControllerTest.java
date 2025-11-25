package com.fin.user.Mockito.ControllerTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fin.user.Controller.UserController;
import com.fin.user.Dto.UserDto;
import com.fin.user.Service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;


    private UserDto userDto;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        userDto = new UserDto(userId, "JohnDoe", "john.doe@example.com", "password123");

    }

    @Test
    void testAddUser() throws Exception {
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        when(userService.addUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/user/" + userDto.getUserId().toString()));
    }

    @Test
    void getUserByUsername() throws Exception {
        when(userService.getUserByUsername(userDto.getUsername())).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/{username}",userDto.getUsername()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("JohnDoe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@example.com"));
    }
}
