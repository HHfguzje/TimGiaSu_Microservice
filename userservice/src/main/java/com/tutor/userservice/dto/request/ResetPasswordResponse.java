package com.tutor.userservice.dto.request;

public class ResetPasswordResponse {
	private String verificationToken;
	private String password;
	private String confirmPassword;
	public ResetPasswordResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public ResetPasswordResponse(String verificationToken, String password, String confirmPassword) {
		super();
		this.verificationToken = verificationToken;
		this.password = password;
		this.confirmPassword = confirmPassword;
	}

	public String getVerificationToken() {
		return verificationToken;
	}
	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
