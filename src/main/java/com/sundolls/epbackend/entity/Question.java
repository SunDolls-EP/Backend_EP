package com.sundolls.epbackend.entity;

import com.sundolls.epbackend.entity.baseEntity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "question_tb")
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class Question extends BaseTimeEntity {
    @Id
    @Column(name = "QUESTION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_ID")
    private User user;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    public Question(Long id, User user, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        setCreatedAt(createdAt);
        setModifiedAt(modifiedAt);
    }

    public void update(String title, String content) {
        if(title!=null){
            this.title = title;
        }
        if(content!=null){
            this.content = content;
        }
    }

}
