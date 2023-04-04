package com.sundolls.epbackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "school_tb")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHOOL_ID")
    private long id;

    @Column(name = "SCHOOL_NAME")
    private String name;
}
