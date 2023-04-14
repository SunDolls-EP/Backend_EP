package com.sundolls.epbackend.domain.entity.primaryKey;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@EqualsAndHashCode
public class StudyInfoId implements Serializable {
    private String userId;
    private long no;
}
