package com.sundolls.epbackend.mapper.impl;

import com.sundolls.epbackend.dto.response.StudyInfoResponse;
import com.sundolls.epbackend.entity.StudyInfo;
import com.sundolls.epbackend.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudyInfoMapper extends EntityMapper<StudyInfoResponse, StudyInfo> {
    StudyInfoMapper MAPPER = Mappers.getMapper(StudyInfoMapper.class);
}
