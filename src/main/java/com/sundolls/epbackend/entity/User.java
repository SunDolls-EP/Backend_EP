package com.sundolls.epbackend.entity;

import com.sundolls.epbackend.entity.baseEntity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_tb")
@NoArgsConstructor
@Getter
public class User extends BaseTimeEntity {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;


    @Column(name="SCHOOL_NAME")
    private String schoolName;



    @Builder
    public User(String id, String username, String password, String schoolName, String email){
        this.id = id;
        this.username = username;
        this.password =password;
        this.schoolName=schoolName;
        this.email = email;
    }

    public void update(String username, String schoolName){
        if(username!=null)
            this.username = username;
        if(schoolName!=null)
            this.schoolName = schoolName;
    }
}
