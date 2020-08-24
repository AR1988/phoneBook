package com.telran.phonebookapi.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

@Service
public class JWTUtil {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    @Value("${com.telran.auth.token.secret}")
    private String jwtSecret;
    @Value("${com.telran.auth.auth.token.expiration}")
    private long expiration;
    public final String accessTokenCookieName = "at";


    public String generateAccessToken(String email) {
        Date date = new Date(System.currentTimeMillis() + expiration);
        Claims claims = new DefaultClaims();
        claims.put("username", email);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public HttpCookie createAccessTokenCookie(String token) {
        LocalTime expirationTime = LocalTime.now().plusSeconds(expiration / 1000);
        return ResponseCookie.from(accessTokenCookieName, token)
                .httpOnly(true)
                .maxAge(Duration.between(LocalTime.now(), expirationTime))
                .path("/")
                .sameSite("lax")
                .build();
    }

    public String extractUsername(String token) {
        return Jwts
                .parser()
                .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token).getBody().get("username", String.class);
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(jwt);

            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
