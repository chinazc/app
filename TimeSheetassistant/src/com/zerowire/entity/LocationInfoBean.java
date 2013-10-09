package com.zerowire.entity;

import java.io.Serializable;
public class LocationInfoBean  implements Serializable {

	/**
	 * Location info bean
	 */
	private static final long serialVersionUID = -3219040588683964358L;
	private String UID;
	private String range;
	private String longitude;
	private String latitude;
	private String project_id;
	private String employee_id;
	
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	
	
}
