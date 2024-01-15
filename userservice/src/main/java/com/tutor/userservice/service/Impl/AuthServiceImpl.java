package com.tutor.userservice.service.Impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tutor.userservice.dto.request.AccountVerificationRequest;
import com.tutor.userservice.dto.request.ChangePasswordRequest;
import com.tutor.userservice.dto.request.EditProfile;
import com.tutor.userservice.dto.request.ResetPasswordResponse;
import com.tutor.userservice.dto.request.SignInRequest;
import com.tutor.userservice.dto.request.SignUpRequest;
import com.tutor.userservice.dto.response.JwtReponse;
import com.tutor.userservice.dto.response.SystemResponse;
import com.tutor.userservice.entities.Role;
import com.tutor.userservice.entities.User;
import com.tutor.userservice.repository.UserRepository;
import com.tutor.userservice.service.AuthService;
import com.tutor.userservice.service.JwtService;

import jakarta.ws.rs.NotFoundException;

@Service
public class AuthServiceImpl implements AuthService {

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
	public SystemResponse signUp(SignUpRequest registerRequest) {
		User user = new User();
		SystemResponse systemResponse = new SystemResponse();
		systemResponse.setTittle("Thông báo hệ thống");
		if (registerRequest.getConfirmPassword().equals(registerRequest.getPassword())) {
			user.setFullName(registerRequest.getFullName());
			user.setAddress(registerRequest.getAddress());
			// Tạo mã thông báo xác nhận
			String verificationToken = UUID.randomUUID().toString();
		    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		    Date date = new Date(System.currentTimeMillis() + 10000 * 60 * 10);
		    String expirationDate = dateFormat.format(date);
		    user.setTimeExpirationToken(expirationDate);
			user.setVerificationToken(verificationToken);
			user.setEmail(registerRequest.getEmail());
			user.setNumber(registerRequest.getNumber());
			user.setUsername(registerRequest.getUsername());
			user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
			if (registerRequest.getRole() == null) {
				// Tự động thêm role gia sư vào nếu chưa có role
				user.setRole(Role.ROLE_TUTOR);
			} else if (registerRequest.getRole().equals("tutor")) {
				user.setRole(Role.ROLE_TUTOR);
			} else if (registerRequest.getRole().equals("admin")) {
				user.setRole(Role.ROLE_ADMIN);
			} else {
				user.setRole(Role.ROLE_STUDENT);
			}

			userRepository.save(user);
			sendVerificationEmail(user);
			systemResponse.setMessage("Đăng ký thành công");
		} else {
			systemResponse.setMessage("Đăng ký thất bại, mật khẩu không trùng khớp");
		}
		return systemResponse;
	}

	@Override
	public SystemResponse accountVerification(AccountVerificationRequest accountVerificationRequest) {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(accountVerificationRequest.getEmail())
				.orElseThrow(() -> new NotFoundException("Email chưa được đăng kí"));
		SystemResponse systemResponse = new SystemResponse();
		systemResponse.setTittle("Thông báo");

		if (user.getVerificationToken().equals(accountVerificationRequest.getToken())) {
			if (convertStringToDate(user.getTimeExpirationToken()).before(new Date())) {
				user.setVerified(true);
				userRepository.save(user);
				systemResponse.setMessage("Tài khoản đã được xác nhận thành công");
			} else {
				systemResponse.setMessage("Mã xác nhận đã hết hạn, vui lòng bấm gửi lại mã mới");
			}
		} else {
			systemResponse.setMessage("Xác nhận tài khoản không thành công, mã xác nhận không đúng");
		}

		return systemResponse;
	}

	private void sendVerificationEmail(User user) {
		String subject = "Xác nhận tai khoan";
		String body = "Chào " + user.getFullName() + ",\n\n"
				+ "Cảm ơn bạn đã đăng ký. Mã xác nhận email của bạn là:\n"
				+ user.getVerificationToken();

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
		JwtReponse jwtReponse = new JwtReponse();
		if (user.isVerified()) {
			var jwt = jwtService.generateToken(user);
			var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
			jwtReponse.setToken(jwt);
			jwtReponse.setRefreshToken(refreshToken);
		} else {
			jwtReponse.setToken("Tài khoản chưa được xác thực");
			jwtReponse.setRefreshToken("Vui lòng xác thực tài khoản");
		}
		return jwtReponse;
	}

	@Override
	public SystemResponse editProfile(EditProfile editProfile) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(editProfile.getUsername())
				.orElseThrow(() -> new NotFoundException("Tên đăng nhập không tồn tại"));
		user.setAddress(editProfile.getAddress());
		user.setFullName(editProfile.getFullName());
		user.setNumber(editProfile.getAddress());
		SystemResponse systemResponse = new SystemResponse();
		userRepository.save(user);
		systemResponse.setTittle("Thông báo hệ thống");
		systemResponse.setMessage("Thay đổi thông tin thành công");

		return systemResponse;
	}

	@Override
	public SystemResponse changePassword(ChangePasswordRequest changePasswordRequest) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(changePasswordRequest.getUsername())
				.orElseThrow(() -> new NotFoundException("Tên đăng nhập không tồn tại"));
		SystemResponse systemResponse = new SystemResponse();
		systemResponse.setTittle("Thông báo hệ thống");
		if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
			if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
				user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
				userRepository.save(user);
				systemResponse.setMessage("Thay đổi mật khâu thành công");
			} else {
				systemResponse.setMessage("Thay đổi mật khẩu thất bại, mật khẩu mới không khớp");
			}
		} else {
			systemResponse.setMessage("Thay đổi mật khẩu thất bại, mật khẩu cũ của bạn không đúng");
		}
		return systemResponse;
	}

	@Override
	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("tên đăng nhập không tồn tại"));
		return user;
	}

	@Override
	public SystemResponse forgetPassword(String email) {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email không tồn tại"));
		SystemResponse systemResponse = new SystemResponse();
		String verificationToken = UUID.randomUUID().toString();
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    Date date = new Date(System.currentTimeMillis() + 10000 * 60 * 10);
	    String expirationDate = dateFormat.format(date);
		user.setVerificationToken(verificationToken);
		user.setTimeExpirationToken(expirationDate);
		userRepository.save(user);
		resetPasswordToken(user);
		systemResponse.setTittle("Thông báo hệ thống");
		systemResponse.setMessage("Mã xác nhận đã được gửi tới gmail của bạn");
		return systemResponse;
	}

	private void resetPasswordToken(User user) {
		String subject = "Xác nhận quên mật khẩu";

		String body = "Chào " + user.getFullName() + ":\n"
				+ "Bạn nhận được email này vì bạn đã yêu cầu đặt lại mật khẩu. Đây là mã đặt lại mật khẩu của bạn:\n"
				+ user.getVerificationToken();
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setSubject(subject);
		message.setText(body);

		javaMailSender.send(message);
	}

	@Override
	public SystemResponse ResetPassword(ResetPasswordResponse resetPasswordResponse) {
		// TODO Auto-generated method stub
		User user = userRepository.findByverificationToken(resetPasswordResponse.getVerificationToken())
				.orElseThrow(() -> new NotFoundException("Mã xác nhận không hợp lệ"));
		SystemResponse systemResponse = new SystemResponse();
		systemResponse.setTittle("Thông báo hệ thống");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        
        if (user.getVerificationToken().equals(resetPasswordResponse.getVerificationToken())) {
        	if (convertStringToDate(user.getTimeExpirationToken()).before(new Date())) {
    			if (resetPasswordResponse.getPassword().equals(resetPasswordResponse.getConfirmPassword())) {
    				user.setPassword(passwordEncoder.encode(resetPasswordResponse.getPassword()));
    				userRepository.save(user);
    				systemResponse.setMessage("Cập nhật mật khẩu mới thành công");
    			} else {
    				systemResponse.setMessage("Mật khẩu mới không khớp"); 
    			}
    		} else {
    			systemResponse.setMessage("Mã xác nhận của bạn đã hết hạn, vui lòng tạo lại mã mới");
    		}
        } else systemResponse.setMessage("Mã xác nhận không đúng");
		return systemResponse;
	}
	
	public static Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
