package com.sundolls.epbackend.filter;


import com.sundolls.epbackend.config.auth.PrincipalDetails;
import com.sundolls.epbackend.config.auth.PrincipalDetailsService;
import com.sundolls.epbackend.entity.User;
import com.sundolls.epbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;


//    private static final ArrayList<String> SWAGGER_PATH = new ArrayList<>(List.of(
//            "/swagger-ui/index.html",
//            "/swagger-ui/swagger-ui.css",
//            "/swagger-ui/index.css",
//            "/swagger-ui/swagger-ui-bundle.js",
//            "/swagger-ui/swagger-ui-standalone-preset.js",
//            "/swagger-ui/swagger-initializer.js",
//            "/swagger-ui/favicon-32x32.png",
//            "/swagger-ui/favicon-16x16.png",
//            "/v3/api-docs/swagger-config",
//            "/v3/api-docs"
//    ));


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            log.info("checkAccessTokenAndAuthentication() called!");
            String accessToken = this.checkAccessTokenAndAuthentication(request);

            if(accessToken == null) {
                throw new AuthenticationCredentialsNotFoundException("jwt Authentication exception occurs!");
            }

            String username = (String) jwtProvider.getPayload(accessToken).getBody().get("username");
            String tag = (String) jwtProvider.getPayload(accessToken).getBody().get("tag");
            PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(username+","+tag);

            Authentication auth = new UsernamePasswordAuthenticationToken(principalDetails.getUser(), principalDetails.getPassword(), principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (AuthenticationCredentialsNotFoundException | UsernameNotFoundException exception) {
            log.error("JwtAuthentication Authentication Exception Occurs! - {}",exception.getClass());
        }
        filterChain.doFilter(request, response);
    }

    private String checkAccessTokenAndAuthentication(HttpServletRequest request) {

        // 1. HttpServletRequest 에서 Access Token 파싱
        Optional<String> token = Optional.ofNullable(request.getHeader("Authorization"));
        if (token.isEmpty() || !this.jwtProvider.validateToken(token.get())) {
            return null;
        }
        return token.get();
    }

}
