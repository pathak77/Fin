package com.fin.ledger_service.Service;

import java.math.BigDecimal;
import java.util.List;

public interface LedgerService {

    com.fin.friend_service.Entity.Ledger addFriend(Long userId, Long friendUserId);

    boolean areFriends(Long userId, Long friendId);

    List<Long> getMyFriends(Long userId);

    com.fin.friend_service.Entity.Ledger getFriendById(Long userId, Long friendId);

    BigDecimal getBalanceWithFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

}
