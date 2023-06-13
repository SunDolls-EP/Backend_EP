package com.sundolls.epbackend.entity;

import com.sundolls.epbackend.entity.baseEntity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_info_tb")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class StudyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INFO_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "TOTAL_STUDY_TIME")
    private Long time;

    @Column(name = "CREATED_AT")
    @CreatedDate
    private LocalDateTime createdAt;

}
