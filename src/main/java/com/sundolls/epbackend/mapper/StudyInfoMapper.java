package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.StudyInfoResponse;
import com.sundolls.epbackend.entity.StudyInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Time;

@Mapper
public interface StudyInfoMapper extends EntityMapper<StudyInfoResponse, StudyInfo> {

    StudyInfoMapper MAPPER = Mappers.getMapper(StudyInfoMapper.class);

    @Override
    @Mapping(
            source = "time", target = "totalTime",
            qualifiedByName = "toTime"
    )
    StudyInfoResponse toDto(final StudyInfo entity);

    @Named("toTime")
    default Time toTime(long second) {
        return new Time(second * 1000);
    }
}
