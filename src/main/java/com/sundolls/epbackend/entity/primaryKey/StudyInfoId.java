package com.sundolls.epbackend.entity.primaryKey;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class StudyInfoId implements Serializable {
    private String userId;

    @Column(name = "INFO_NO")
    private long no;
}
