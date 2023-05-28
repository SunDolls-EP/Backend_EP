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

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;


    @Column(name="SCHOOL_NAME")
    private String schoolName;

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

    public void update(String username, String schoolName){
        if(username!=null)
            this.username = username;
        if(schoolName!=null)
            this.schoolName = schoolName;
    }
}
