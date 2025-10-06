package com.example.employee.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final Key key;
    private final long jwtExpirationMs;

    public JwtUtil(@Value("${jwt.secret}") String base64Secret,
                   @Value("${jwt.expiration-ms}") long jwtExpirationMs) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(UserDetails userDetails) {
        var roles = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

	public boolean validateToken(String token, UserDetails userDetails) {
		boolean isValid = false;
		try {
			final String username = extractUsername(token);
			isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		} catch (JwtException | IllegalArgumentException e) {
			System.err.println("Error in getClaims: " + e);
		}

		return isValid;

	}

	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();		
	}

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
