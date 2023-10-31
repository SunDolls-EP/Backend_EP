package com.sundolls.epbackend.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.sundolls.epbackend.config.auth.PrincipalDetailsService;
import com.sundolls.epbackend.filter.JwtAuthenticationFilter;
import com.sundolls.epbackend.filter.JwtProvider;
import com.sundolls.epbackend.repository.UserRepository;
import com.sundolls.epbackend.repository.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@ServletComponentScan
@RequiredArgsConstructor
@PropertySource("/oauth.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.ios-client-id}")
    private String iosClientId;
    @Value("${spring.security.oauth2.client.registration.google.android-client-id}")
    private String androidClientId;


    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier(){
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                        new GsonFactory())
                .setAudience(Arrays.asList(clientId, iosClientId, androidClientId))
                .build();
    }


    private static final String[] SWAGGER_PATH = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private static final String[] AUTH_FREE_PATH = {
            "/api/rank/**",
            "/api/rank"
    };


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(SWAGGER_PATH)
                .antMatchers("/api/auth/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers(AUTH_FREE_PATH).permitAll()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, principalDetailsService), UsernamePasswordAuthenticationFilter.class)
        ;
    }

}