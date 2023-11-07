package com.sundolls.epbackend.config;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sundolls.epbackend.filter.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {
    @PersistenceContext
    EntityManager entityManager;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public NetHttpTransport netHttpTransport(){
        return new NetHttpTransport();
    }
    @Bean
    GsonFactory gsonFactory(){
        return new GsonFactory();
    }
    @Bean
    JwtProvider jwtProvider(){return new JwtProvider();}
    @Bean
    public JPAQueryFactory jpaQueryFactory(){return new JPAQueryFactory(entityManager);}

}
