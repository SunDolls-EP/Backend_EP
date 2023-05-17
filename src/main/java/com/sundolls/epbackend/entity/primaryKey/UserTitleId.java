package com.sundolls.epbackend.entity.primaryKey;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class UserTitleId implements Serializable {
     String id;
     Long titleId;
}
