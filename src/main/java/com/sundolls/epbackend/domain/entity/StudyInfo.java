package com.sundolls.epbackend.domain.entity;

import com.sundolls.epbackend.domain.entity.primaryKey.StudyInfoId;

import javax.persistence.*;

@Entity
@Table(name = "study_info_tb")
public class StudyInfo extends BaseTimeEntity{

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "USER_UID")
    private User user;

    @EmbeddedId
    private StudyInfoId studyInfoId;

}
