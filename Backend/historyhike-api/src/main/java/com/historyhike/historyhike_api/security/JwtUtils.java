package com.historyhike.historyhike_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Log the key in base64 format for easy comparison
    private void logKey() {
        System.out.println("JWT Secret Key: " + Base64.getEncoder().encodeToString(key.getEncoded()));
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        logKey();
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        logKey();
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        logKey();
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        logKey();
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(String token) {
        logKey();
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, String username) {
        logKey();
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }
}