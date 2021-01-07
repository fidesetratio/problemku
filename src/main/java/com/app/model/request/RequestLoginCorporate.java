package com.app.model.request;

import java.io.Serializable;

public class RequestLoginCorporate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5437279006241725226L;
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