package com.tutor.userservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tutor.userservice.dto.request.AccountVerificationRequest;
import com.tutor.userservice.dto.request.ResetPasswordResponse;
import com.tutor.userservice.dto.request.SignInRequest;
import com.tutor.userservice.dto.request.SignUpRequest;
import com.tutor.userservice.dto.response.JwtReponse;
import com.tutor.userservice.dto.response.SystemResponse;
import com.tutor.userservice.entities.User;
import com.tutor.userservice.repository.UserRepository;
import com.tutor.userservice.service.AuthService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService service;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/signup")
	public ResponseEntity<SystemResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
		return ResponseEntity.ok(service.signUp(signUpRequest));
	}

	@PostMapping("/signin")
	public ResponseEntity<JwtReponse> signIn(@RequestBody SignInRequest signInRequest) {
		return ResponseEntity.ok(service.signIn(signInRequest));
	}

	@PutMapping("/verification")
	public ResponseEntity<SystemResponse> xacNhan(@RequestBody AccountVerificationRequest accountVerificationRequest) {
		return ResponseEntity.ok(service.accountVerification(accountVerificationRequest));
	}

	@PostMapping("/forget-password")
	public ResponseEntity<SystemResponse> forgotPassword(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		service.forgetPassword(email);
		return ResponseEntity.ok(service.forgetPassword(email));
	}

	@PutMapping("/reset-password")
	public ResponseEntity<SystemResponse> resetPassword(@RequestBody ResetPasswordResponse resetPasswordResponse) {
		return ResponseEntity.ok(service.ResetPassword(resetPasswordResponse));

	}
}
