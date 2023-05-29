package com.sundolls.epbackend.repository;

import com.sundolls.epbackend.entity.Question;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.repository.impl.QuestionRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void saveTest(){
        User user = userRepository.findById("FOR_TEST").get();

        for (int i = 0; i < 20; i++) {
            Question question = Question.builder()
                    .user(user)
                    .title("Title"+i)
                    .content("Content: "+i)
                    .build();
            questionRepository.save(question);
        }
    }

}
