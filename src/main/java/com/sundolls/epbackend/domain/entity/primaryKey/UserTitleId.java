package com.sundolls.epbackend.domain.entity.primaryKey;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class UserTitleId implements Serializable {
    private String userId;
    private Long titleId;
}
