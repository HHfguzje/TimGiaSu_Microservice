package com.tutor.userservice.service.Impl;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tutor.userservice.dto.request.ChangePasswordRequest;
import com.tutor.userservice.dto.request.EditProfile;
import com.tutor.userservice.dto.request.SignInRequest;
import com.tutor.userservice.dto.request.SignUpRequest;
import com.tutor.userservice.dto.response.JwtReponse;
import com.tutor.userservice.entities.Role;
import com.tutor.userservice.entities.User;
import com.tutor.userservice.repository.UserRepository;
import com.tutor.userservice.service.AuthService;
import com.tutor.userservice.service.JwtService;

import jakarta.ws.rs.NotFoundException;

@Service
public class AuthServiceImpl implements AuthService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	public User signUp(SignUpRequest registerRequest) {
		User user = new User();
		user.setFullName(registerRequest.getFullName());
		user.setAddress(registerRequest.getAddress());
		// Tạo mã thông báo xác nhận
	    String verificationToken = UUID.randomUUID().toString();
	    user.setVerificationToken(verificationToken);
	    
		user.setEmail(registerRequest.getEmail());
		user.setNumber(registerRequest.getNumber());
		user.setUsername(registerRequest.getUsername());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

		if (registerRequest.getRole() == null) {
			// Tự động thêm role gia sư vào nếu chưa có role
			user.setRole(Role.ROLE_TUTOR);
		} else if (registerRequest.getRole().equals("tutor")){
			user.setRole(Role.ROLE_TUTOR);
		} else if (registerRequest.getRole().equals("admin")) {
			user.setRole(Role.ROLE_ADMIN);
		} else {
			user.setRole(Role.ROLE_STUDENT);
		}
		
		userRepository.save(user);
		// Gửi email xác nhận
	    sendVerificationEmail(user);
	    
		return user;
	}
	
	private void sendVerificationEmail(User user) {
	    String subject = "Xác nhận tai khoan";
	    String confirmationUrl = "http://giasuviet.vn/xac-nhan?token=" + user.getVerificationToken();

	    String body = "Chào " + user.getFullName() + ",\n\n"
	            + "Cảm ơn bạn đã đăng ký. Vui lòng nhấp vào liên kết bên dưới để xác nhận email của bạn:\n"
	            + confirmationUrl;

	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(user.getEmail());
	    message.setSubject(subject);
	    message.setText(body);

	    javaMailSender.send(message);
	}
	
	@Override
	public JwtReponse signIn(SignInRequest signInRequest) {
		// TODO Auto-generated method stub
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
		var user = userRepository.findByUsername(signInRequest.getUsername())
				.orElseThrow(() -> new IllegalArgumentException("Tài khoản hoặc mật khẩu không hợp lệ"));
		var jwt = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
		
		JwtReponse jwtReponse = new JwtReponse();
		jwtReponse.setToken(jwt);
		jwtReponse.setRefreshToken(refreshToken);
		
		return jwtReponse;
	}

	@Override
	public User editProfile(EditProfile editProfile) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(editProfile.getUsername()).orElseThrow(()-> new NotFoundException("Tên đăng nhập không tồn tại"));
		user.setAddress(editProfile.getAddress());
		user.setFullName(editProfile.getFullName());
		user.setNumber(editProfile.getAddress());
		return userRepository.save(user);
	}

	@Override
	public User changePassword(ChangePasswordRequest changePasswordRequest) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(changePasswordRequest.getUsername()).orElseThrow(()-> new NotFoundException("Tên đăng nhập không tồn tại"));
		if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
			if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
				user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
			     userRepository.save(user);
			}
		}
		return user;
	}

	@Override
	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(username)
				.orElseThrow(()-> new NotFoundException("tên đăng nhập không tồn tại"));
		return user;
	}
	
	
}
