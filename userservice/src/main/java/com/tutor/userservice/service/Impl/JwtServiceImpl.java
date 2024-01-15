package com.tutor.userservice.service.Impl;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.tutor.userservice.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

	// tạo token từ key
	@Override
	public String generateToken(UserDetails userDetails) {
		// TODO Auto-generated method stub
		return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
				.signWith(getSigninkey(), SignatureAlgorithm.HS256).compact();
	}

	// lấy username từ token
	@Override
	public String extractUsername(String token) {
	    try {
	        return extractClaim(token, Claims::getSubject);
	    } catch (Exception e) {
	        // Ghi log hoặc xử lý ngoại lệ, ví dụ: in ra đám cháy
	        e.printStackTrace();
	        return null;
	    }
	}

	// Kiểm tra token hợp lệ
	@Override
	public boolean isTokenValid(String token, UserDetails userDetails) {
		// TODO Auto-generated method stub
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	// So sánh thời gian hết hạn với tgian hiện tại
	@Override
	public boolean isTokenExpired(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	// Trích xuất jwt từ token
	@Override
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
		// TODO Auto-generated method stub
		final Claims claims = extractAllClaims(token);
		return claimsResolvers.apply(claims);
	}

	// Truy xuất xác nhận quyền sở hữu từ token
	@Override
	public Claims extractAllClaims(String token) {
		// TODO Auto-generated method stub
		return Jwts.parserBuilder().setSigningKey(getSigninkey()).build().parseClaimsJws(token).getBody();
	}

	// Lấy key đăng nhập
	@Override
	public Key getSigninkey() {
		// TODO Auto-generated method stub
		byte[] key = Decoders.BASE64.decode("413F4428472B4B6250655368566D5970337336763979244226452948404D6351");
		return Keys.hmacShaKeyFor(key);
	}

	@Override
	public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		// TODO Auto-generated method stub
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+ 604800000))
				.signWith(getSigninkey(), SignatureAlgorithm.HS256)
				.compact();
	}

}
