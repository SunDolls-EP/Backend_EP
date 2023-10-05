package com.sundolls.epbackend.repository;

import com.sundolls.epbackend.config.TestConfig;
import com.sundolls.epbackend.dto.response.RankResponse;
import com.sundolls.epbackend.entity.User;

import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@EnableConfigurationProperties
@Import(TestConfig.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void insertUser(){
        ArrayList<String> schools = new ArrayList<>(Arrays.asList("1","2","3","4","5","6","7","8","9","0"));
        for(int i = 0; i<100; i++) {
            Random random = new Random();
            User user = User.builder()
                    .id("test_"+i)
                    .email(String.valueOf(i)+"test")
                    .username(String.valueOf(i))
                    .tag("1234")
                    .schoolName(schools.get(i%10))
                    .totalStudyTime(random.nextInt(324790))
                    .build();
            userRepository.save(user);
        }
    }

    @Test
    public void rankSchoolQueryTest(){
        List<RankResponse> rankResponses = userRepository.getSchoolRank(10);

        for ( RankResponse response: rankResponses) {
            System.out.println(response.getName()+": "+response.getTotalStudyTime());
        }
    }

    @Test
    public void rankInSchoolQueryTest(){
        List<RankResponse> rankResponses = userRepository.getPersonalRank("c",10);

        for ( RankResponse response: rankResponses) {
            System.out.println(response.getName()+": "+response.getTotalStudyTime());
        }
    }

    @Test
    public void rankPersonalQueryTest(){
        List<RankResponse> rankResponses = userRepository.getPersonalRank(50);

        for ( RankResponse response: rankResponses) {
            System.out.println(response.getName()+": "+response.getTotalStudyTime());
        }
    }
}
