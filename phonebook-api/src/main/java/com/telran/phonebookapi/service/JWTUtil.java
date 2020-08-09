package com.telran.phonebookapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JWTUtil {

    @Value("${com.telran.auth.jwt.secret}")
    String jwtSecret;

    public String generateAccessToken(String email) {
        Date date = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Claims claims = new DefaultClaims();
        claims.put("username", email);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    private Date extractExpiration(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    Claims parseToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return parseToken(token).get("username", String.class);
    }
}
