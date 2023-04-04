package com.sundolls.epbackend.entity.primaryKey;

import com.sundolls.epbackend.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Data
public class CalendarId implements Serializable {

    private String userId;

    @Column(name = "CONTENT_NO")
    private long no;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
