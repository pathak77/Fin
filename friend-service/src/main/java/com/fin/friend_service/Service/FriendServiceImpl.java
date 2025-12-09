package com.fin.friend_service.Service;

import com.fin.friend_service.Entity.Friend;
import com.fin.friend_service.FriendRepository.FriendRepository;
import com.fin.friend_service.GlobalExceptions.BadRequestException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;

    FriendServiceImpl(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }
    @Override
    public boolean areFriends(Long userAId, Long userBId) {
        if (userAId.equals(userBId)) return false;

        return friendRepository.existsByUserOneIdAndUserTwoId(userAId, userBId) ||
                friendRepository.existsByUserOneIdAndUserTwoId(userBId, userAId);
    }


    @Override
    @Transactional
    public Friend addFriend(Long userAId, Long userBId) {


        if (userAId.equals(userBId)) {
            throw new IllegalArgumentException("A user cannot be friends with themselves.");
        }

        if (areFriends(userAId, userBId)) {
            throw new RuntimeException("Users are already friends.");
        }

        Long smallerId = Math.min(userAId, userBId);
        Long largerId = Math.max(userAId, userBId);

        Friend newFriend = new Friend();
        newFriend.setUserOneId(smallerId);
        newFriend.setUserTwoId(largerId);
        newFriend.setCreatedAt(ZonedDateTime.now());

        return friendRepository.save(newFriend);
    }

    @Override
    public List<Friend> getMyFriends(Long userId) {
        return List.of();
    }

    @Override
    public Friend getFriendById(Long userId, Long friendId) {
        return null;
    }

    @Override
    public BigDecimal getBalanceWithFriend(Long userId, Long friendId) {
        return null;
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {

    }


    @Override
    public List<Long> getFriends(Long userId) {
        List<Friend> relationships = friendRepository.findAllByUserOneIdOrUserTwoId(userId, userId);

        return relationships.stream()
                .map(friend -> {
                    // Return the ID that is NOT the current userId
                    if (friend.getUserOneId().equals(userId)) {
                        return friend.getUserTwoId();
                    } else {
                        return friend.getUserOneId();
                    }
                })
                .collect(Collectors.toList());
    }
}
