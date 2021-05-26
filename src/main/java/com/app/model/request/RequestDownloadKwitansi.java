package com.app.model.request;

import java.io.Serializable;

public class RequestDownloadKwitansi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6844831461013071962L;
	private String username;
	private String key;
	private String path;
	
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
