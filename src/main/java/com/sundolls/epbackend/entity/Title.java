package com.sundolls.epbackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "title_tb")
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TITLE_ID")
    private long id;

    @Column(name = "TITLE_NAME")
    private String name;

    @Column(name = "TITLE_DESC")
    private String desc;
}
