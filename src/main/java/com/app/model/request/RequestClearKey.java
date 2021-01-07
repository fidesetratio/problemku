package com.app.model.request;

import java.io.Serializable;

public class RequestClearKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5706811386709874451L;
	private String username;
	private String key;
	private String token;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getToken() {
		return token;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
