package com.fin.friend_service.Service;

import com.fin.friend_service.Entity.Friend;
import com.fin.friend_service.FriendRepository.FriendRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


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
        newFriend.setCreatedAt((LocalDate.now()));

        return friendRepository.save(newFriend);
    }

    @Override
    public List<Long> getMyFriends(Long friendId) {
        List<Friend> relationships = friendRepository.findAllByUserOneIdOrUserTwoId(friendId, friendId);

        return relationships.stream()
                .map(friend -> {
                    if (friend.getUserOneId().equals(friendId)) {
                        return friend.getUserTwoId();
                    } else {
                        return friend.getUserOneId();
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public Friend getFriendById(Long userId, Long friendId) {
        Friend friend = friendRepository.findById(friendId).orElse(null);
        if (friend == null) {
            throw new IllegalArgumentException("Friend does not exist.");
        }
        return friend;
    }

    @Override
    public BigDecimal getBalanceWithFriend(Long userId, Long friendId) {
        return null;
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("A user cannot be friends with themselves.");
        }
        boolean isFriend = friendRepository.existsByUserOneIdAndUserTwoId(userId, friendId);

        if (!isFriend) {
            throw new RuntimeException("User is not friend.");
        }
        friendRepository.deleteFriendById(userId, friendId);
    }

}
