package com.tutor.userservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tutor.userservice.dto.request.SignInRequest;
import com.tutor.userservice.dto.request.SignUpRequest;
import com.tutor.userservice.dto.response.JwtReponse;
import com.tutor.userservice.entities.User;

public interface UserService {
	UserDetailsService userDetailsService();
}
