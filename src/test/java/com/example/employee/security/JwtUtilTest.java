package com.example.employee.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testSecret = "dGhpc2lzYXRlc3RzZWNyZXRrZXl0aGF0c2hvdWxkYmVhdGxlYXN0MjU2Yml0c2xvbmc=";
    private final long testExpirationMs = 86400000; // 24 hours
    private UserDetails testUser;
    
    @BeforeEach
    void setUp() {
        // Create JwtUtil instance with test values
        jwtUtil = new JwtUtil(testSecret, testExpirationMs);
        
        // Create test user with authorities
        Collection<GrantedAuthority> authorities = Arrays.asList(
            new SimpleGrantedAuthority("ROLE_USER"),
            new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        testUser = new User("testuser", "password", authorities);
    }

    @Test
    void generateToken_ShouldCreateValidJwt() {
        // When
        String token = jwtUtil.generateToken(testUser);
        
        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verify token structure (JWT has 3 parts separated by dots)
        String[] tokenParts = token.split("\\.");
        assertEquals(3, tokenParts.length);
    }

    @Test
    void generateToken_ShouldIncludeCorrectClaims() {
        // When
        String token = jwtUtil.generateToken(testUser);
        
        // Then
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(testSecret));
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        
        assertEquals("testuser", claims.getSubject());
        assertEquals("ROLE_ADMIN,ROLE_USER", claims.get("roles"));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
        
        // Verify expiration is set correctly
        long expectedExpiration = claims.getIssuedAt().getTime() + testExpirationMs;
        assertEquals(expectedExpiration, claims.getExpiration().getTime());
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        // Given
        String token = jwtUtil.generateToken(testUser);
        
        // When
        String extractedUsername = jwtUtil.extractUsername(token);
        
        // Then
        assertEquals("testuser", extractedUsername);
    }

    @Test
    void extractUsername_WithInvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid.token.here";
        
        // When & Then
        assertThrows(JwtException.class, () -> jwtUtil.extractUsername(invalidToken));
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Given
        String token = jwtUtil.generateToken(testUser);
        
        // When
        boolean isValid = jwtUtil.validateToken(token, testUser);
        
        // Then
        assertTrue(isValid);
    }

    @Test
    void validateToken_WithWrongUsername_ShouldReturnFalse() {
        // Given
        String token = jwtUtil.generateToken(testUser);
        UserDetails wrongUser = new User("wronguser", "password", testUser.getAuthorities());
        
        // When
        boolean isValid = jwtUtil.validateToken(token, wrongUser);
        
        // Then
        assertFalse(isValid);
    }

    @Test
    void validateToken_WithExpiredToken_ShouldReturnFalse() {
        // Given - Create JwtUtil with very short expiration
        JwtUtil shortExpirationJwtUtil = new JwtUtil(testSecret, 1); // 1ms expiration
        String token = shortExpirationJwtUtil.generateToken(testUser);
        
        // Wait for token to expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // When
        boolean isValid = shortExpirationJwtUtil.validateToken(token, testUser);
        
        // Then
        assertFalse(isValid);
    }

    @Test
    void validateToken_WithMalformedToken_ShouldThrowException() {
        // Given
        String malformedToken = "malformed.token";
        
        // When & Then
        assertFalse(jwtUtil.validateToken(malformedToken, testUser));
    }

    @Test
    void generateToken_WithUserWithoutAuthorities_ShouldCreateTokenWithEmptyRoles() {
        // Given
        UserDetails userWithoutAuthorities = new User("usernoauth", "password", Arrays.asList());
        
        // When
        String token = jwtUtil.generateToken(userWithoutAuthorities);
        
        // Then
        assertNotNull(token);
        
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(testSecret));
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        
        assertEquals("usernoauth", claims.getSubject());
        assertEquals("", claims.get("roles"));
    }

    @Test
    void generateToken_WithSingleAuthority_ShouldCreateTokenWithSingleRole() {
        // Given
        UserDetails userWithSingleAuth = new User("singleauth", "password", 
            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        
        // When
        String token = jwtUtil.generateToken(userWithSingleAuth);
        
        // Then
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(testSecret));
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        
        assertEquals("singleauth", claims.getSubject());
        assertEquals("ROLE_USER", claims.get("roles"));
    }

    @Test
    void jwtUtil_WithInvalidSecret_ShouldThrowException() {
        // Given
        String invalidSecret = "tooshort";
        
        // When & Then
        assertThrows(Exception.class, () -> new JwtUtil(invalidSecret, testExpirationMs));
    }
}