package com.app.model.request;

import java.io.Serializable;

public class RequestStatementDownload implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6773845513937718133L;
	private String username;
	private String key;
	private String no_polis;
	private Integer request_code;
	private String file_name;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public Integer getRequest_code() {
		return request_code;
	}

	public String getFile_name() {
		return file_name;
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

	public void setRequest_code(Integer request_code) {
		this.request_code = request_code;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
}
