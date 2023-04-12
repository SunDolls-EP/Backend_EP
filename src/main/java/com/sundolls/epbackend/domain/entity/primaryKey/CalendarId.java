package com.sundolls.epbackend.domain.entity.primaryKey;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;


@Data
@EqualsAndHashCode
public class CalendarId implements Serializable {
    private String userId;
    private long no;
}
