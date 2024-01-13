package com.tutor.userservice.dto.request;

public class ChangePasswordRequest {
	private String username;
	private String oldPassword;
	private String newPassword;
	private String confirmNewPassword;
	public ChangePasswordRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChangePasswordRequest(String username, String oldPassword, String newPassword, String confirmNewPassword) {
		super();
		this.username = username;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.confirmNewPassword = confirmNewPassword;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}
	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}
	

}
