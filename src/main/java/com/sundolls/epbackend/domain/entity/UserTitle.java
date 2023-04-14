package com.sundolls.epbackend.domain.entity;

import com.sundolls.epbackend.domain.entity.primaryKey.UserTitleId;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_title_tb")
@NoArgsConstructor
public class UserTitle {

    @EmbeddedId
    private UserTitleId userTitleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TITLE_ID")
    @MapsId("titleId")
    private Title title;


}
