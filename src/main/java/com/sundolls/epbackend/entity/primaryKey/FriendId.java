package com.sundolls.epbackend.entity.primaryKey;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Setter
@Getter
public class FriendId implements Serializable {
    String userId;
    String targetId;
}
