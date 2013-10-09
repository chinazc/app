package com.zerowire.entity;

import java.io.Serializable;

public class FlyBackBean implements Serializable{
	
	private static final long serialVersionUID = -4405665432954289081L;
	private String flybackName;
	private String flybackId;
	public String getFlybackName() {
		return flybackName;
	}
	public void setFlybackName(String flybackName) {
		this.flybackName = flybackName;
	}
	public String getFlybackId() {
		return flybackId;
	}
	public void setFlybackId(String flybackId) {
		this.flybackId = flybackId;
	}
	

}
