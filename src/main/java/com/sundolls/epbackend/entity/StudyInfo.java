package com.sundolls.epbackend.entity;

import com.sundolls.epbackend.entity.baseEntity.BaseTimeEntity;
import com.sundolls.epbackend.entity.primaryKey.StudyInfoId;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "study_info_tb")
@NoArgsConstructor
public class StudyInfo extends BaseTimeEntity {

    @EmbeddedId
    private StudyInfoId studyInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @MapsId("id")
    private User user;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INFO_NO")
    private long no;

}
