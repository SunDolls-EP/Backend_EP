package com.sundolls.epbackend.mapper.impl;

import com.sundolls.epbackend.dto.response.StudyInfoResponse;
import com.sundolls.epbackend.entity.StudyInfo;
import com.sundolls.epbackend.mapper.StudyInfoMapper;
import org.springframework.stereotype.Component;

@Component
public class StudyInfoMapperImpl implements StudyInfoMapper {
    @Override
    public StudyInfoResponse toDto(StudyInfo entity) {
        StudyInfoResponse studyInfoResponse = new StudyInfoResponse();
        studyInfoResponse.setStartAt(entity.getCreatedAt());
        studyInfoResponse.setTotalTime(entity.getTime());

        return studyInfoResponse;
    }
}
