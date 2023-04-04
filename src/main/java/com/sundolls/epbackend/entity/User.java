package com.sundolls.epbackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_tb")
public class User extends BaseTimeEntity{
    @Id
    @Column(name = "UID")
    private String uid;

    @Column(name = "NICKNAME")
    private String nickName;

    @OneToOne
    @JoinColumn(name = "SCHOOL_ID")
    private School school;


}
