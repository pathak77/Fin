package com.fin.friend_service.Service;

import com.fin.friend_service.Dto.FriendDto;
import com.fin.friend_service.Entity.Friend;

import java.math.BigDecimal;
import java.util.List;

public interface FriendService<T> {

    Friend addFriend(Long userId, Long friendUserId);

    boolean areFriends(Long userId, Long friendId);

    List<Friend> getMyFriends(Long userId);

    Friend getFriendById(Long userId, Long friendId);

    BigDecimal getBalanceWithFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

}
