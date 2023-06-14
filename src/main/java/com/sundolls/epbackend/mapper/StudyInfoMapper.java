package com.sundolls.epbackend.mapper;

import com.sundolls.epbackend.dto.response.StudyInfoResponse;
import com.sundolls.epbackend.entity.StudyInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Mapper
public interface StudyInfoMapper extends EntityMapper<StudyInfoResponse, StudyInfo> {

    StudyInfoMapper MAPPER = Mappers.getMapper(StudyInfoMapper.class);

    @Override
    @Mapping(
            source = "time", target = "totalTime",
            qualifiedByName = "toTimeString"
    )
    @Mapping(
            source = "createdAt", target = "startAt"
    )
    @Mapping(
            target = "endAt", expression = "java(getEndAt(entity.getCreatedAt(), entity.getTime()))"
    )
    StudyInfoResponse toDto(final StudyInfo entity);

    @Named("toTimeString")
    default Time toTimeString(long seconds) {
        int S = (int) (seconds % 60);
        int H = (int) (seconds / 60);
        int M = H % 60;
        H = H / 60;
        return Time.valueOf(LocalTime.of(H,M,S));
    }

    @Named("getEndAt")
    default LocalDateTime getEndAt(LocalDateTime startAt, long time) {
        return startAt.plusSeconds(time);
    }
}
