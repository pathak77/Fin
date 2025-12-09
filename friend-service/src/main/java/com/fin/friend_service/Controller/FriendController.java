package com.fin.friend_service.Controller;

import com.fin.friend_service.Entity.Friend;
import com.fin.friend_service.Service.FriendServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
public class FriendController {

    FriendServiceImpl friendService;

     public FriendController(FriendServiceImpl friendService) {
        this.friendService = friendService;
    }


}
