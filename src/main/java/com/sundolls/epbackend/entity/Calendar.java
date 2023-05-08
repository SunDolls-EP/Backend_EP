package com.sundolls.epbackend.entity;

import com.sundolls.epbackend.entity.baseEntity.BaseTimeEntity;
import com.sundolls.epbackend.entity.primaryKey.CalendarId;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "calendar_content_tb")
@NoArgsConstructor
public class Calendar extends BaseTimeEntity {

    @EmbeddedId
    private CalendarId calendarId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @MapsId("id")
    private User user;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CALENDAR_NO")
    private long no;

    @Column(name = "CONTENT")
    private String content;
}
