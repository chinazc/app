package com.zerowire.entity;

import java.io.Serializable;
public class EmployeeBean  implements Serializable {
	/**
	 * Employee Entity Bean
	 */
	private static final long serialVersionUID = -4409339432954289081L;
	private String employeeId;
	private String backgroundImgId;
	private String avatarImgId;
	private String signature;
	private String name;
	private String positionName;
	
	private String phoneNumber;
	private String email;

	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getBackgroundImgId() {
		return backgroundImgId;
	}
	public void setBackgroundImgId(String backgroundImgId) {
		this.backgroundImgId = backgroundImgId;
	}
	public String getAvatarImgId() {
		return avatarImgId;
	}
	public void setAvatarImgId(String avatarImgId) {
		this.avatarImgId = avatarImgId;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}


	
}
