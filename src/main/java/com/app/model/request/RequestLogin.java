package com.app.model.request;

import java.io.Serializable;

public class RequestLogin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2133765021607922869L;
	private String username;
	private String password;
	private String last_login_device;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getLast_login_device() {
		return last_login_device;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setLast_login_device(String last_login_device) {
		this.last_login_device = last_login_device;
	}
}
