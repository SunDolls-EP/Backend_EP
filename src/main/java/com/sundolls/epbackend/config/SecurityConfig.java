package com.sundolls.epbackend.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.sundolls.epbackend.config.auth.PrincipalDetailsService;
import com.sundolls.epbackend.filter.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@ServletComponentScan
@RequiredArgsConstructor
@PropertySource("/oauth.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PrincipalDetailsService principalDetailsService;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public NetHttpTransport netHttpTransport(){
        return new NetHttpTransport();
    }
    @Bean GsonFactory gsonFactory(){
        return new GsonFactory();
    }
    @Bean
    JwtProvider jwtProvider(){
        return new JwtProvider(principalDetailsService);
    }


    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier(){
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                        new GsonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    private static final String[] SWAGGER_PATH = {
            "/swagger*/**",
            "/swagger-ui.html",
            "/swagger-resources*/**",
            "/v2/api-docs"
    };
    private static final String[] USER_PATH = {
            "/user*/**"
    };
    private static final String[] OAUTH2_PATH = {
            "/oauth2*/**"
    };

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(SWAGGER_PATH)
                .antMatchers(USER_PATH)
                .antMatchers(OAUTH2_PATH);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {

        http

                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/api/token/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .cors().configurationSource(request -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.setAllowedOrigins(List.of("*"));
                    cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS","PATCH"));
                    cors.setAllowedHeaders(List.of("*"));
                    return cors;
                })

        ;
    }

}