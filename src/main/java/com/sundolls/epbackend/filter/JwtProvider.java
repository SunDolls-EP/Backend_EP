package com.sundolls.epbackend.filter;

import com.sundolls.epbackend.config.auth.PrincipalDetails;
import com.sundolls.epbackend.config.auth.PrincipalDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@PropertySource("/jwt.properties")
@RequiredArgsConstructor
public class JwtProvider {
    private final PrincipalDetailsService principalDetailsService;

    @Value("${jwt.secret}")
    private String secret;

    private final long VALID_MILLISECOND = 1000L *60 * 10;

    private Key getSecretKey(String secret){
        byte[] KeyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(KeyBytes);
    }

    //Username: DB 테이블의 Id
    public String getUsername(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey(secret))
                .build()
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }

    public String generateToken(String id){
        Date date = new Date();
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime()+VALID_MILLISECOND))
                .signWith(getSecretKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey(secret))
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e){
            return false;
        }
    }


    public Authentication getAuthentication(String jwtToken) {
        PrincipalDetails principalDetails = principalDetailsService.loadUserByUsername(getUsername(jwtToken));
        return new UsernamePasswordAuthenticationToken(principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities());
    }

}
