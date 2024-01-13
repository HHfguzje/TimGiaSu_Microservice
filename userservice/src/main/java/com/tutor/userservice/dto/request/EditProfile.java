package com.tutor.userservice.dto.request;

public class EditProfile {
	private String username;
	private String fullName;
	private String address;
	private String number;
	public EditProfile() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EditProfile(String username, String fullName, String address, String number) {
		super();
		this.username = username;
		this.fullName = fullName;
		this.address = address;
		this.number = number;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	
}
