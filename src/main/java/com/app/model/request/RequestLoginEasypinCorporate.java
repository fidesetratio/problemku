package com.app.model.request;

import java.io.Serializable;

public class RequestLoginEasypinCorporate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9213621769729384851L;
	private String username;
	private String last_login_device;

	public String getUsername() {
		return username;
	}

	public String getLast_login_device() {
		return last_login_device;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setLast_login_device(String last_login_device) {
		this.last_login_device = last_login_device;
	}
}