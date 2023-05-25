package com.sundolls.epbackend.repository.impl;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sundolls.epbackend.entity.Question;
import com.sundolls.epbackend.repository.QuestionRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.sundolls.epbackend.entity.QQuestion.question;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Question> searchQuestions(Pageable pageable, String username, String title, String content, LocalDateTime from, LocalDateTime to) {

        JPAQuery<Question> query = queryFactory.selectFrom(question)
                .where(eqUsername(username), eqTitle(title), eqContent(content), eqCreatedAt(from, to));

    }

    private BooleanExpression eqUsername(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }
        return question.user.username.eq(username);
    }

    private BooleanExpression eqTitle(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }
        return question.title.eq(title);
    }

    private BooleanExpression eqContent(String content) {
        if (content == null || content.isEmpty()) {
            return null;
        }
        return question.content.eq(content);
    }

    private BooleanExpression eqCreatedAt(LocalDateTime from, LocalDateTime to) {
        return question.createdAt.between(from, to);
    }
}
