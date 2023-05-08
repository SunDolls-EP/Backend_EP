package com.sundolls.epbackend.entity.primaryKey;

import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class FriendId implements Serializable {
    long userId;
    long targetId;
}
