package com.tutor.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tutor.userservice.dto.request.SignInRequest;
import com.tutor.userservice.dto.request.SignUpRequest;
import com.tutor.userservice.dto.response.JwtReponse;
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
	public ResponseEntity<User> signUp(@RequestBody SignUpRequest signUpRequest) {
		return ResponseEntity.ok(service.signUp(signUpRequest));
	}

	@PostMapping("/signin")
	public ResponseEntity<JwtReponse> signIn(@RequestBody SignInRequest signInRequest) {
		return ResponseEntity.ok(service.signIn(signInRequest));
	}

	@GetMapping("/verification")
	public ResponseEntity<User> xacNhan(@RequestParam("token") String token) {
		User user = userRepository.findByVerificationToken(token)
				.orElseThrow(() -> new IllegalArgumentException("Mã xác nhận không hợp lệ"));

		user.setVerified(true);
		userRepository.save(user);

		// Trả về ResponseEntity với thông điệp
		return ResponseEntity.ok(userRepository.save(user));
	}

}
