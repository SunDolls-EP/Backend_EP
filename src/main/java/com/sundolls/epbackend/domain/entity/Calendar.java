package com.sundolls.epbackend.domain.entity;

import com.sundolls.epbackend.domain.entity.baseEntity.BaseTimeEntity;
import com.sundolls.epbackend.domain.entity.primaryKey.CalendarId;

import javax.persistence.*;

@Entity
@Table(name = "calendar_content_tb")
@IdClass(CalendarId.class)
public class Calendar extends BaseTimeEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CALENDAR_NO")
    private long no;

    @Column(name = "CONTENT")
    private String content;
}
