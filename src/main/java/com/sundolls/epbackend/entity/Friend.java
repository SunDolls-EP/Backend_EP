package com.sundolls.epbackend.entity;


import com.sundolls.epbackend.entity.primaryKey.FriendId;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "friend_tb")
public class Friend {

    @EmbeddedId
    private FriendId friendId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TARGET_ID")
    @MapsId("targetId")
    private User targetUser;

    @Column(name="ACCEPTED")
    private boolean accepted;

}