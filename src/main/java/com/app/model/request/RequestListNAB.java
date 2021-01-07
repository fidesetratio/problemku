package com.app.model.request;

import java.io.Serializable;

public class RequestListNAB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6734123559173231931L;
	private String username;
	private String key;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
