package com.epam.aditechtask.util;

import com.epam.aditechtask.persistence.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration;

	private Key signingKey;

	@PostConstruct
	public void init() {
		byte[] keyBytes = secret.getBytes();
		this.signingKey = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(String username, Set<Role> roles) {
		return Jwts.builder()
			.subject(username)
			.claim("roles", roles.stream().map(Enum::name).collect(Collectors.toList()))
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(signingKey)
			.compact();
	}

	public String extractUsername(String token) {
		return getClaims(token).getSubject();
	}

	public Set<Role> extractRoles(String token) {
		return ((List<?>) getClaims(token).get("roles")).stream()
			.map(role -> Role.valueOf(role.toString()))
			.collect(Collectors.toSet());
	}

	public boolean validateToken(String token) {
		try {
			getClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Claims getClaims(String token) {
		return Jwts.parser()
			.verifyWith((SecretKey) signingKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
