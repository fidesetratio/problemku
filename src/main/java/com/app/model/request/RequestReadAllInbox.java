package com.app.model.request;

import java.io.Serializable;

public class RequestReadAllInbox implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3040289431045019870L;
	private String username;
	private String key;
	private String new_status;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNew_status() {
		return new_status;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setNew_status(String new_status) {
		this.new_status = new_status;
	}
}
