package com.udise.portal.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "jai-bholenath";

    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public  boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        System.out.println("line 33 in JwtUtil "+extractedUsername);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public static String extractUsername(String token) {
        String sub= extractAllClaims(token).getSubject();
        System.out.println(sub);
        return sub;
    }

    private static Claims extractAllClaims(String token) {
        Claims cls= Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        System.out.println(cls.toString());
        return cls;
    }

    private static boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}

