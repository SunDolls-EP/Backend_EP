package com.sundolls.epbackend.domain.entity.primaryKey;

import javax.persistence.Column;
import java.io.Serializable;

public class StudyInfoId implements Serializable {
    private String userId;

    @Column(name = "INFO_NO")
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
