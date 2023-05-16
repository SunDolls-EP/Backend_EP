package com.sundolls.epbackend.config;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.sundolls.epbackend.config.auth.PrincipalDetailsService;
import com.sundolls.epbackend.filter.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {
    private final PrincipalDetailsService principalDetailsService;

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
    JwtProvider jwtProvider(){
        return new JwtProvider(principalDetailsService);
    }
}
