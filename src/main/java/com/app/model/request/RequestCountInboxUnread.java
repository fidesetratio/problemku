package com.app.model.request;

import java.io.Serializable;

public class RequestCountInboxUnread implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2361612154166185974L;
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
