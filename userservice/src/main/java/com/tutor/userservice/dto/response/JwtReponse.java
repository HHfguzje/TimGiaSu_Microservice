package com.tutor.userservice.dto.response;

public class JwtReponse {
	private String token;
	private String refreshToken;
	public JwtReponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public JwtReponse(String token, String refreshToken) {
		super();
		this.token = token;
		this.refreshToken = refreshToken;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
