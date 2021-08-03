package com.app.model.request;

import java.io.Serializable;

public class RequestCheckEnableClaimCorp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2563403099623172487L;
	private String username;
	private String key;
	private String no_polis;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}
}