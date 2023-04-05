package com.sundolls.epbackend.domain.entity;

import lombok.Builder;

import javax.persistence.*;

@Entity
@Table(name = "question_tb")
public class Question extends BaseTimeEntity{
    @Id
    @Column(name = "QUESTION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "WRITER_UID")
    private User user;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Builder
    public Question(User user, String title, String content){
    };

}