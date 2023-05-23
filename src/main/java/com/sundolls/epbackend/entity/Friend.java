package com.sundolls.epbackend.entity;


import com.sundolls.epbackend.entity.baseEntity.BaseTimeEntity;
import com.sundolls.epbackend.entity.primaryKey.FriendId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "friend_tb")
@Getter
@Builder
@AllArgsConstructor
public class Friend extends BaseTimeEntity {

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

    public void accept(){
        accepted = true;
    }
}
