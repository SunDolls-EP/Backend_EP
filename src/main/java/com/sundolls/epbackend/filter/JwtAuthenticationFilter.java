package com.sundolls.epbackend.filter;


import com.sundolls.epbackend.config.auth.PrincipalDetails;
import com.sundolls.epbackend.config.auth.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;

    private static final ArrayList<String> SWAGGER_PATH = new ArrayList<>(List.of(
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
    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = parseJwt(request);
        if(jwtProvider.validateToken(token)){
            PrincipalDetails principalDetails = principalDetailsService.loadUserByUsername(jwtProvider.getPayload(token));
            Authentication auth = new UsernamePasswordAuthenticationToken(principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
        } else if(SWAGGER_PATH.contains(request.getServletPath())){

        } else {
            response.setStatus(401);
       }
        filterChain.doFilter(request,response);
    }

    private String parseJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");
        if(StringUtils.hasText(headerAuth)){
            return headerAuth;
        }
        return null;
    }

}
