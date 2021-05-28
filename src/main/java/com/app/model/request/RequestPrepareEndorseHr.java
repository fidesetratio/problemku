package com.app.model.request;

import java.io.Serializable;

public class RequestPrepareEndorseHr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7593991121805604265L;
	private String username;
	private String key;
	private String no_polis;
	
	public String getNo_polis() {
		return no_polis;
	}
	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}
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
}