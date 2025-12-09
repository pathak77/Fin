package com.fin.friend_service.Controller;


import com.fin.friend_service.Dto.FriendResponseDto;
import com.fin.friend_service.Dto.FriendSummaryDto;
import com.fin.friend_service.Entity.Friend;
import com.fin.friend_service.GlobalExceptions.BadRequestException;
import com.fin.friend_service.Service.FriendService;
import com.fin.friend_service.Service.FriendServiceImpl;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("{userId}/friends")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendServiceImpl friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/{friendId}")
    public ResponseEntity<FriendResponseDto> addFriend(
           @Valid
            @PathVariable Long userId,
            @PathVariable Long friendId) {

            Friend newFriendship = friendService.addFriend(userId, friendId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new FriendResponseDto(newFriendship.getUserOneId(), newFriendship.getUserTwoId()));
    }

    @GetMapping
    public ResponseEntity<FriendSummaryDto> getMyFriends(
            @PathVariable
            Long userId) {

        List<Long> friendIds = friendService.getMyFriends(userId);
        FriendSummaryDto summary = FriendSummaryDto
                .builder()
                .userOneId(userId)
                .friendList(friendIds)
                .build();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{friendId}/status")
    public ResponseEntity<Map<String, Boolean>> checkFriendStatus(
            @PathVariable Long userId,
            @PathVariable Long friendId) {
        boolean isFriend = friendService.areFriends(userId, friendId);
        return ResponseEntity.ok(Map.of("areFriends", isFriend));
    }

    @GetMapping("/{friendId}/balance")
    public ResponseEntity<BigDecimal> getBalanceWithFriend(
            @PathVariable Long userId,
            @PathVariable Long friendId) {
        BigDecimal balance = friendService.getBalanceWithFriend(userId, friendId);
        return ResponseEntity.ok(balance);
    }

    @DeleteMapping("/{friendId}/remove")
    public void removeFriend(
            @PathVariable Long userId,
            @PathVariable Long friendId) {
        try {
            friendService.removeFriend(userId, friendId);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
