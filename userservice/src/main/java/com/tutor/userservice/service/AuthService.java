package com.tutor.userservice.service;

import com.tutor.userservice.dto.request.AccountVerificationRequest;
import com.tutor.userservice.dto.request.ChangePasswordRequest;
import com.tutor.userservice.dto.request.EditProfile;
import com.tutor.userservice.dto.request.ResetPasswordResponse;
import com.tutor.userservice.dto.request.SignInRequest;
import com.tutor.userservice.dto.request.SignUpRequest;
import com.tutor.userservice.dto.response.JwtReponse;
import com.tutor.userservice.dto.response.SystemResponse;
import com.tutor.userservice.entities.User;

public interface AuthService {
	SystemResponse signUp(SignUpRequest signUpRequest);

	JwtReponse signIn(SignInRequest signInRequest);
	
	SystemResponse editProfile(EditProfile editProfile);
	
	SystemResponse changePassword(ChangePasswordRequest changePasswordRequest);
	
	User getUserByUsername(String username);
	
	SystemResponse forgetPassword(String email);
	
	SystemResponse ResetPassword(ResetPasswordResponse resetPasswordResponse);
	
	SystemResponse accountVerification(AccountVerificationRequest accountVerificationRequest);
}
