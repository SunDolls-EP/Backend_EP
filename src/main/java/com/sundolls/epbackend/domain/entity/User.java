package com.sundolls.epbackend.domain.entity;

import com.sundolls.epbackend.domain.dto.UserDto;
import com.sundolls.epbackend.domain.entity.baseEntity.BaseTimeEntity;
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

    @Column(name="ROLE")
    private String role;

    @Column(name="SCHOOL_NAME")
    private String schoolName;


    @Column(name="PROVIDER")
    private String provider;

    @Column(name = "PROVIDER_ID")
    private String providerId;

    @Builder
    public User(String id, String username, String school, String email, String role, String provider, String providerId){
        this.id = id;
        this.username = username;
        this.schoolName=school;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public UserDto toUserDto(){
        /**
         * userDto를 반환 하는 메소드
         */
        return null;
    }

}
