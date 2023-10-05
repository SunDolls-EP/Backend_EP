package com.sundolls.epbackend.config;

import com.sundolls.epbackend.config.auth.PrincipalDetailsService;
import com.sundolls.epbackend.filter.JwtAuthenticationFilter;
import com.sundolls.epbackend.filter.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class FilterConfig extends AbstractHttpConfigurer<FilterConfig, HttpSecurity> {
    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, principalDetailsService), UsernamePasswordAuthenticationFilter.class);
    }
}
