package com.tutor.userservice.dto.request;

public class AccountVerificationRequest {
	private String token;
	private String email;
	public AccountVerificationRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AccountVerificationRequest(String token, String email) {
		super();
		this.token = token;
		this.email = email;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
