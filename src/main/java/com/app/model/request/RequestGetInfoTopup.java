package com.app.model.request;

import java.io.Serializable;

public class RequestGetInfoTopup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4013355136731362868L;
	private String username;
	private String key;
	private String no_polis;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}
}
