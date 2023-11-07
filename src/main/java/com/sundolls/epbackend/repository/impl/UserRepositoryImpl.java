package com.sundolls.epbackend.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sundolls.epbackend.dto.response.SchoolRankResponse;
import com.sundolls.epbackend.dto.response.UserResponse;
import com.sundolls.epbackend.entity.QUser;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.mapper.UserMapper;
import com.sundolls.epbackend.repository.UserRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.sundolls.epbackend.entity.QUser.user;


@Repository
@Slf4j
public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        super(QUser.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<SchoolRankResponse> getSchoolRank(Integer limit) {
        JPAQuery<Tuple> query = queryFactory.select(user.schoolName, user.totalStudyTime.sum().as("totalStudyTime"))
                .from(user)
                .groupBy(user.schoolName)
                .orderBy(user.totalStudyTime.desc())
                .limit(limitTo50(limit));

        List<Tuple> result = query.fetch();
        List<SchoolRankResponse> responses = new ArrayList<>(result.stream().map(tuple -> new SchoolRankResponse(tuple.get(user.schoolName), tuple.get(user.totalStudyTime.sum().as("totalStudyTime")))).toList());
        responses.removeIf(response -> response.getName() == null);


        return responses;
    }

    @Override
    public List<UserResponse> getPersonalRank(String schoolName, Integer limit) {
        JPAQuery<User> query = queryFactory.select(user)
                .from(user)
                .where(eqSchoolName(schoolName))
                .orderBy(user.totalStudyTime.desc())
                .limit(limitTo50(limit));

        List<User> result = query.fetch();
        return result.stream().map(UserMapper::toDto).toList();
    }

    @Override
    public List<UserResponse> getPersonalRank(Integer limit) {
        return getPersonalRank(null, limit);
    }

    private Integer limitTo50(Integer limit) {
        if (limit==null || limit>50) {
            return 10;
        }
        else {
            return limit;
        }
    }

    public BooleanExpression eqSchoolName(String schoolName) {
        if (schoolName == null || schoolName.isEmpty()) {
            return null;
        }
        return user.schoolName.eq(schoolName);
    }

}
