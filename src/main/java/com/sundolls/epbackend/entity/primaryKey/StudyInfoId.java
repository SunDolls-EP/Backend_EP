package com.sundolls.epbackend.entity.primaryKey;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@EqualsAndHashCode
public class StudyInfoId implements Serializable {
     String id;
     Long no;
}
