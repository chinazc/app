package com.zerowire.entity;

public class PageBean {
	private String id;
	private String rb1;
	private String rb2;
	private String date;
	
	
	
	public PageBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PageBean(String id,String rb1, String rb2, String date) {
		super();
		this.id=id;
		this.rb1 = rb1;
		this.rb2 = rb2;
		this.date = date;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRb1() {
		return rb1;
	}
	public void setRb1(String rb1) {
		this.rb1 = rb1;
	}
	public String getRb2() {
		return rb2;
	}
	public void setRb2(String rb2) {
		this.rb2 = rb2;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
