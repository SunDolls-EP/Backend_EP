package com.sundolls.epbackend.entity;

import com.sundolls.epbackend.entity.primaryKey.UserTitleId;
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
    @MapsId("id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TITLE_ID")
    @MapsId("titleId")
    private Title title;


}
