package com.fin.ledger_service.Service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LedgerServiceImpl implements LedgerService {

    private final com.fin.friend_service.FriendRepository.LedgerRepository ledgerRepository;

    LedgerServiceImpl(com.fin.friend_service.FriendRepository.LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }


    @Override
    public boolean areFriends(Long userAId, Long userBId) {
        if (userAId.equals(userBId)) return false;

        return ledgerRepository.existsByUserOneIdAndUserTwoId(userAId, userBId) ||
                ledgerRepository.existsByUserOneIdAndUserTwoId(userBId, userAId);
    }


    @Override
    @Transactional
    public com.fin.friend_service.Entity.Ledger addFriend(Long userAId, Long userBId) {

        if (userAId.equals(userBId)) {
            throw new IllegalArgumentException("A user cannot be friends with themselves.");
        }

        if (areFriends(userAId, userBId)) {
            throw new RuntimeException("Users are already friends.");
        }

        Long smallerId = Math.min(userAId, userBId);
        Long largerId = Math.max(userAId, userBId);

        com.fin.friend_service.Entity.Ledger newLedger = new com.fin.friend_service.Entity.Ledger();
        newLedger.setUserOneId(smallerId);
        newLedger.setUserTwoId(largerId);
        newLedger.setCreatedAt((LocalDate.now()));

        return ledgerRepository.save(newLedger);
    }

    @Override
    public List<Long> getMyFriends(Long friendId) {
        List<com.fin.friend_service.Entity.Ledger> relationships = ledgerRepository.findAllByUserOneIdOrUserTwoId(friendId, friendId);

        return relationships.stream()
                .map(ledger -> {
                    if (ledger.getUserOneId().equals(friendId)) {
                        return ledger.getUserTwoId();
                    } else {
                        return ledger.getUserOneId();
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public com.fin.friend_service.Entity.Ledger getFriendById(Long userId, Long friendId) {
        com.fin.friend_service.Entity.Ledger ledger = ledgerRepository.findById(friendId).orElse(null);
        if (ledger == null) {
            throw new IllegalArgumentException("Friend does not exist.");
        }
        return ledger;
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
        boolean isFriend = ledgerRepository.existsByUserOneIdAndUserTwoId(userId, friendId);

        if (!isFriend) {
            throw new RuntimeException("User is not friend.");
        }
        ledgerRepository.deleteFriendById(userId, friendId);
    }

}
