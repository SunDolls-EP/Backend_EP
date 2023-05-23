package com.sundolls.epbackend.repository;

import com.sundolls.epbackend.entity.Friend;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.entity.primaryKey.FriendId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {
    ArrayList<Friend> findAllByUser(User user);
    ArrayList<Friend> findAllByTargetUser(User targetUser);

    Optional<Friend> findByUserAndTargetUser(User user, User targetUser);
}
