package com.sundolls.epbackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "answer_tb")
public class Answer extends BaseTimeEntity{
    @Id
    @Column(name = "ANSWER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @OneToOne
    @JoinColumn(name = "WRITER_UID")
    private User user;

    @Column(name = "CONTENT")
    private String content;

}
