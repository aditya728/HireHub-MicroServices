package com.charlie.hirehub.userservice.user.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(AuthenticatedUser authenticatedUser){

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", authenticatedUser.getUser().getId());
        claims.put("role", authenticatedUser.getUser().getRole().name());

        return Jwts.builder()
                .claims(claims)
                .subject(authenticatedUser.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token) {

        return !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
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
