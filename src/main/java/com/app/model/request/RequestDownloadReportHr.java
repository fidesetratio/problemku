package com.app.model.request;

import java.io.Serializable;

public class RequestDownloadReportHr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6844831461013071962L;
	private String username;
	private String key;
	private String no_polis;
	private String no_batch;
	
	public String getNo_batch() {
		return no_batch;
	}

	public void setNo_batch(String no_batch) {
		this.no_batch = no_batch;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

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
