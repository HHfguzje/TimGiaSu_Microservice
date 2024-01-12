package com.tutor.userservice.service;

import java.security.Key;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.base.Function;

import io.jsonwebtoken.Claims;

public interface JwtService {
	String generateToken(UserDetails userDetails);
	String extractUsername (String token);
	boolean isTokenValid(String token, UserDetails userDetails);
	boolean isTokenExpired(String token);
	<T> T extractClaim(String token, Function<Claims, T> claimsResolvers);
	Claims extractAllClaims(String token);
	Key getSigninkey();
	String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
