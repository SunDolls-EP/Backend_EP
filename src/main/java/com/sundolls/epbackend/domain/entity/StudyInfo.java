package com.sundolls.epbackend.domain.entity;

import com.sundolls.epbackend.domain.entity.baseEntity.BaseTimeEntity;
import com.sundolls.epbackend.domain.entity.primaryKey.StudyInfoId;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "study_info_tb")
@NoArgsConstructor
@IdClass(StudyInfoId.class)
public class StudyInfo extends BaseTimeEntity {

    @EmbeddedId
    StudyInfoId studyInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @MapsId("userId")
    private User user;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INFO_NO")
    @MapsId("no")
    private Long no;

}
