package com.sundolls.epbackend.entity;

import com.sundolls.epbackend.entity.primaryKey.CalendarId;

import javax.persistence.*;

@Entity
@Table(name = "calendar_content_tb")
public class Calendar extends BaseTimeEntity{

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "USER_UID")
    private User user;

    @EmbeddedId
    private CalendarId calendarId;

    @Column(name = "CONTENT")
    private String content;
}
