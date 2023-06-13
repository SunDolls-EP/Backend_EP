package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.StudyInfoResponse;
import com.sundolls.epbackend.entity.StudyInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Time;
import java.time.LocalDateTime;

@Mapper
public interface StudyInfoMapper extends EntityMapper<StudyInfoResponse, StudyInfo> {

    StudyInfoMapper MAPPER = Mappers.getMapper(StudyInfoMapper.class);

    @Override
    @Mapping(
            source = "time", target = "totalTime",
            qualifiedByName = "toTime"
    )
    @Mapping(
            source = "createdAt", target = "startAt"
    )
    @Mapping(
            target = "endAt", expression = "java(getEndAt(entity.getCreatedAt(), entity.getTime()))"
    )
    StudyInfoResponse toDto(final StudyInfo entity);

    @Named("toTime")
    default Time toTime(long second) {
        return new Time(second * 1000);
    }

    @Named("getEndAt")
    default LocalDateTime getEndAt(LocalDateTime startAt, long time) {
        return startAt.plusSeconds(time);
    }
}
