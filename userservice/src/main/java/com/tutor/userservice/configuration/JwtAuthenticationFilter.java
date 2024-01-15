package com.tutor.userservice.configuration;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tutor.userservice.service.JwtService;
import com.tutor.userservice.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final UserService userService;

	private final JwtService jwtService;

	@Autowired
	public JwtAuthenticationFilter(UserService userService, JwtService jwtService) {
		super();
		this.userService = userService;
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authenHeader = request.getHeader("Authorization");
		// Lấy token từ username
		final String jwt;
		final String username;
		System.out.println(authenHeader);
		// header phải bắt đầu bằng bearer
		if (StringUtils.isEmpty(authenHeader) || !StringUtils.startsWith(authenHeader, "Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = StringUtils.substringAfter(authenHeader, "Bearer ");
		username = jwtService.extractUsername(jwt);
		System.out.println(jwt);
		UserDetails userDetails = null;
		if (username != null) {
			 userDetails = userService.userDetailsService().loadUserByUsername(username);
		} else {
			System.out.println("username null");
		}

		if (userDetails != null && jwtService.isTokenValid(jwt, userDetails)) {
			System.out.println("Token is VALID!");
			SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			securityContext.setAuthentication(authenticationToken);
			SecurityContextHolder.setContext(securityContext);
		} else {
			System.out.println("Token is NOT VALID!");
		}

		// kiem username hop le chua duoc dang nhap
		if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails1 = userService.userDetailsService().loadUserByUsername(username);
			// kiem tra tinh hop le cua nguoi dung, neu hop le thi tao
			// UsernamePasswordAuthenticationToken, va cap nhat SecurityContext
			if (jwtService.isTokenValid(jwt, userDetails1)) {
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails1, null, userDetails1.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				securityContext.setAuthentication(authenticationToken);
				SecurityContextHolder.setContext(securityContext);
			}
		}

		filterChain.doFilter(request, response);
	}
}
