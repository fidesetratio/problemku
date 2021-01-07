package com.app.model.request;

import java.io.Serializable;

public class RequestLastActivity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9149647499059640543L;
	private String username;
	private String token;

	public String getUsername() {
		return username;
	}

	public String getToken() {
		return token;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
