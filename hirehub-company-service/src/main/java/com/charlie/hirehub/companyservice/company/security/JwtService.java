package com.charlie.hirehub.companyservice.company.security;

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

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Method to extract any claim from the JWT
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extract all claims at once and share to JwtFilter
    // to avoid parsing the Token 3 times for userId, role, email
    public AuthenticatedPrincipal getAuthenticatedPrincipal(String token) {

        Claims claims = extractAllClaims(token);

        return new AuthenticatedPrincipal(
                claims.get("userId", Long.class),
                claims.getSubject(),
                claims.get("role", String.class)
        );
    }

    // Generic method to extract any particular type of claim. Eg: username, issuedAt, expiration etc.
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }
}
