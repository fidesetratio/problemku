package com.app.model.request;

import java.io.Serializable;

public class RequestDeleteAllInbox implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9036323470096842726L;
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
