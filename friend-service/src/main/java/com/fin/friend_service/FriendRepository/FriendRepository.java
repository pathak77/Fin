package com.fin.friend_service.FriendRepository;

import com.fin.friend_service.Entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    boolean existsByUserOneIdAndUserTwoId(Long userOneId, Long userTwoId);

    List<Friend> findAllByUserOneIdOrUserTwoId(Long userOneId, Long userTwoId);
}
