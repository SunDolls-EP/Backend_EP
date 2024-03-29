package com.sundolls.epbackend.entity;

import com.sundolls.epbackend.entity.baseEntity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_tb")
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "TAG")
    private String tag;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name="SCHOOL_NAME")
    private String schoolName;

    @Column(name="TOTAL_STUDY_TIME")
    private Integer totalStudyTime;

    @Column(name="PROFILE_IMAGE_URL")
    private String profileUrl;

    @Override
    public boolean equals(Object obj) {
        User user;
        if (obj instanceof User) {
            user = (User) obj;
        } else {
            return false;
        }
        return this.id.equals(user.getId());
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", tag='" + tag + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", totalStudyTime=" + totalStudyTime +
                ", profileUrl='" + profileUrl + '\'' +
                '}';
    }

    public void update(String username, String schoolName, String tag, String profileUrl){
        if(username!=null)
            this.username = username;
        if(schoolName!=null)
            this.schoolName = schoolName;
        if (username != null)
            this.tag = tag;
        if (profileUrl != null)
            this.profileUrl = profileUrl;
    }

    public void addTime(Integer studyTime) {
        this.totalStudyTime += studyTime;
    }
}
