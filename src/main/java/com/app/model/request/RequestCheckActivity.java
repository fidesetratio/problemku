package com.app.model.request;

import java.io.Serializable;

public class RequestCheckActivity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2675801675313599158L;
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
