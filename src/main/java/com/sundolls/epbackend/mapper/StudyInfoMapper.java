package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.StudyInfoResponse;
import com.sundolls.epbackend.entity.StudyInfo;

public class StudyInfoMapper {


    public static StudyInfoResponse toDto(StudyInfo entity) {
        StudyInfoResponse studyInfoResponse = new StudyInfoResponse();
        studyInfoResponse.setStartAt(entity.getCreatedAt());
        studyInfoResponse.setTotalTime(entity.getTime());

        return studyInfoResponse;
    }
}
