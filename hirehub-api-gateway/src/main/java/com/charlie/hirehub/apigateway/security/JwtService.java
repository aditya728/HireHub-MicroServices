package com.charlie.hirehub.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Method to extract any claim from the JWT
    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public AuthenticatedPrincipal getAuthenticatedPrincipal(String token){
        Claims claims = extractAllClaims(token);

        return new AuthenticatedPrincipal(
                claims.get("userId", Long.class),
                claims.getSubject(),
                Role.valueOf(claims.get("role", String.class))
        );
    }
}
