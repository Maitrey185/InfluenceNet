package com.project.InfluenceNet.auth.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkey12345"; // must be at least 32 chars

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Default token validity: 1 hour
    private static final long DEFAULT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000; // 24 hours
    
    public String generateToken(String username) {
        return generateToken(username, DEFAULT_TOKEN_VALIDITY);
    }
    
    public String generateToken(String username, long validityInMilliseconds) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return getClaimsFromToken(token).getSubject();
    }
    
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }
    
    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true; // If we can't parse the token, consider it expired
        }
    }
    
    private io.jsonwebtoken.Claims getClaimsFromToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                throw new RuntimeException("Token is null or empty");
            }
            
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .setAllowedClockSkewSeconds(60) // Allow 1 minute clock skew
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            throw new RuntimeException("JWT token has expired", ex);
        } catch (io.jsonwebtoken.MalformedJwtException ex) {
            throw new RuntimeException("Invalid JWT token", ex);
        } catch (io.jsonwebtoken.UnsupportedJwtException ex) {
            throw new RuntimeException("Unsupported JWT token", ex);
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            throw new RuntimeException("JWT signature does not match", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error processing JWT token: " + ex.getMessage(), ex);
        }
    }


}
