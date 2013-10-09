package com.zerowire.webservice;

public class WebServiceAttribute {
	private String nameSpace;
	private String serviceURL;
	private String methodName;
	private String soapAction;
	private String realURL;

	public String getRealURL() {
		return realURL;
	}

	public void setRealURL(String realURL) {
		this.realURL = realURL;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getSoapAction() {
		soapAction = nameSpace + "/" + methodName;
		return soapAction;
	}



}
