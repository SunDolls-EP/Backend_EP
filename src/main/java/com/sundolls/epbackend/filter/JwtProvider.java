package com.sundolls.epbackend.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@PropertySource("/jwt.properties")
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    private final long VALID_MILLISECOND = 1000L *60 * 30;  //30분

    private final long REFRESH_VALID_MILLISECOND = 1000L * 60 * 60 * 24 * 7;  //일주일
    private Key getSecretKey(String secret){
        byte[] KeyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(KeyBytes);
    }

    //Username:
    public Jws<Claims> getPayload(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey(secret))
                .build()
                .parseClaimsJws(jwtToken);
    }

    public String generateAccessToken(String username, String tag){
        Date date = new Date();
        Map<String, String> payload = new HashMap<>();
        payload.put("username",username);
        payload.put("tag",tag);

        return Jwts.builder()
                .setClaims(payload)
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

    public String generateRefreshToken(String email){
        Date date = new Date();

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime()+REFRESH_VALID_MILLISECOND))
                .signWith(getSecretKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

}
