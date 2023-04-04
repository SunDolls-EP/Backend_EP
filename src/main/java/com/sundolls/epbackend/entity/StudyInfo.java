package com.sundolls.epbackend.entity;

import com.sundolls.epbackend.entity.primaryKey.StudyInfoId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;

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
