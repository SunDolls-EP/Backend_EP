package com.sundolls.epbackend.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sundolls.epbackend.entity.QQuestion;
import com.sundolls.epbackend.entity.Question;
import com.sundolls.epbackend.repository.QuestionRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

import static com.sundolls.epbackend.entity.QQuestion.question;

@Repository
public class QuestionRepositoryImpl extends QuerydslRepositorySupport implements QuestionRepositoryCustom {


    private final JPAQueryFactory queryFactory;

    public QuestionRepositoryImpl (JPAQueryFactory queryFactory) {
        super(QQuestion.class);
        this.queryFactory = queryFactory;
    }


    @Override
    public Page<Question> searchQuestions(Pageable pageable, String username, String tag, String title, String content, LocalDateTime from, LocalDateTime to) {

        JPAQuery<Tuple> query = queryFactory.select(question.id, question.title, question.user, question.createdAt, question.modifiedAt).from(question)
                .where(eqUsername(username), eqTag(tag), containsTitle(title), containsContent(content), betweenCreatedAt(from, to));

        int totalCount = query.fetch().size();
        List<Tuple> questionTuple = getQuerydsl().applyPagination(pageable, query).fetch();

        List<Question> questions = questionTuple.stream().map(this::toQuestion).toList();

        return new PageImpl<Question>(questions, pageable, totalCount);
    }


    private BooleanExpression eqUsername(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }
        return question.user.username.eq(username);
    }

    private BooleanExpression eqTag(String tag) {
        if (tag == null || tag.isEmpty()) {
            return null;
        }
        return question.user.tag.eq(tag);
    }

    private BooleanExpression containsTitle(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }
        return question.title.contains(title);
    }

    private BooleanExpression containsContent(String content) {
        if (content == null || content.isEmpty()) {
            return null;
        }
        return question.content.contains(content);
    }

    private BooleanExpression betweenCreatedAt(LocalDateTime from, LocalDateTime to) {
        return question.createdAt.between(from, to);
    }

    private Question toQuestion(Tuple tuple) {
        Question questionEntity = Question.builder()
                .id(tuple.get(question.id))
                .title(tuple.get(question.title))
                .user(tuple.get(question.user))
                .build();
        questionEntity.setCreatedAt(tuple.get(question.createdAt));
        questionEntity.setModifiedAt(tuple.get(question.modifiedAt));

        return questionEntity;
    }

}
