package com.sundolls.epbackend.domain.entity;

import com.sundolls.epbackend.domain.entity.baseEntity.BaseTimeEntity;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "answer_tb")
@NoArgsConstructor
public class Answer extends BaseTimeEntity {
    @Id
    @Column(name = "ANSWER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_ID")
    private User user;

    @Column(name = "CONTENT")
    private String content;

}
