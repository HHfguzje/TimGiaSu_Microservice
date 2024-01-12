package com.tutor.userservice.service;

import com.tutor.userservice.dto.request.SignInRequest;
import com.tutor.userservice.dto.request.SignUpRequest;
import com.tutor.userservice.dto.response.JwtReponse;
import com.tutor.userservice.entities.User;

public interface AuthService {
	User signUp(SignUpRequest signUpRequest);

	JwtReponse signIn(SignInRequest signInRequest);
}