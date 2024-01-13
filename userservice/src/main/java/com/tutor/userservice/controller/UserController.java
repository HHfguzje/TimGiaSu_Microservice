package com.tutor.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tutor.userservice.dto.request.ChangePasswordRequest;
import com.tutor.userservice.dto.request.EditProfile;
import com.tutor.userservice.entities.User;
import com.tutor.userservice.repository.UserRepository;
import com.tutor.userservice.service.AuthService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private AuthService service;

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/")
	public ResponseEntity<User> getUserProfile(@RequestParam("username") String username){
		User user = service.getUserByUsername(username);
		return ResponseEntity.ok(user);
	}
	
	@PutMapping("/edit-profile")
	public ResponseEntity<User> updateProfile(@RequestBody EditProfile editProfile){
		return ResponseEntity.ok(service.editProfile(editProfile));
	}
	
	@PutMapping("/change-password")
	public ResponseEntity<User> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
		return ResponseEntity.ok(service.changePassword(changePasswordRequest));
	}
	
}
