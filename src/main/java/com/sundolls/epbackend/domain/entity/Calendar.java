package com.sundolls.epbackend.domain.entity;

import com.sundolls.epbackend.domain.entity.baseEntity.BaseTimeEntity;
import com.sundolls.epbackend.domain.entity.primaryKey.CalendarId;
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
    @MapsId("userId")
    private User user;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CALENDAR_NO")
    @MapsId("no")
    private long no;

    @Column(name = "CONTENT")
    private String content;
}
