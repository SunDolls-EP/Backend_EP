package com.sundolls.epbackend.entity;

import com.sundolls.epbackend.entity.baseEntity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "calendar_content_tb")
@NoArgsConstructor
@Getter
public class Calendar extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CALENDAR_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "CONTENT")
    private String content;

    @Builder
    public Calendar(User user, String content){
        this.user = user;
        this.content = content;
    }

    public void update(String content){
        this.content = content;
    }

}
