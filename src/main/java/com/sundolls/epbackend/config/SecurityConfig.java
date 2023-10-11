package com.sundolls.epbackend.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.sundolls.epbackend.config.auth.PrincipalDetailsService;
import com.sundolls.epbackend.config.oauth.PrincipalOauth2UserService;
import com.sundolls.epbackend.filter.JwtAuthenticationFilter;
import com.sundolls.epbackend.filter.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

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


    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier(){
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                        new GsonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    private static final String[] SWAGGER_PATH = {
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/index.css",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/swagger-initializer.js",
            "/swagger-ui/favicon-32x32.png",
            "/swagger-ui/favicon-16x16.png",
            "/v3/api-docs/swagger-config",
            "/v3/api-docs"
    };

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http

                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers(SWAGGER_PATH).permitAll()
                .antMatchers(HttpMethod.GET).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, principalDetailsService), UsernamePasswordAuthenticationFilter.class)
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