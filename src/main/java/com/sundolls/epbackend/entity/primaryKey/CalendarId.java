package com.sundolls.epbackend.entity.primaryKey;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;


@Embeddable
@EqualsAndHashCode
public class CalendarId implements Serializable {
     String id;
     Long no;
}
