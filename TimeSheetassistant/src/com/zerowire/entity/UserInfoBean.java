package com.zerowire.entity;

import java.io.Serializable;
public class UserInfoBean  implements Serializable {

	/**
	 * UserInfo
	 */
	private static final long serialVersionUID = -7825804799577798586L;
	private String username = null;
	private String pwd = null;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	
	
}
