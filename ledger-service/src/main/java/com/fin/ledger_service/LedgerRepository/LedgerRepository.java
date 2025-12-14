package com.fin.friend_service.FriendRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LedgerRepository extends JpaRepository<com.fin.friend_service.Entity.Ledger, Long> {

    boolean existsByUserOneIdAndUserTwoId(Long userOneId, Long userTwoId);

    List<com.fin.friend_service.Entity.Ledger> findAllByUserOneIdOrUserTwoId(Long userOneId, Long userTwoId);

    com.fin.friend_service.Entity.Ledger getFriendById(Long userId, Long friendId);

    void deleteFriendById(Long userId, Long friendId);
}
