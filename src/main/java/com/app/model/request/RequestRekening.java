package com.app.model.request;

import java.io.Serializable;

public class RequestRekening implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3923939802324387543L;
	private String username;
	private String key;
	private String no_polis;
	private Integer language_id;
	private Integer keyId = 0;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public Integer getLanguage_id() {
		return language_id;
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

	public void setLanguage_id(Integer language_id) {
		this.language_id = language_id;
	}

	public Integer getKeyId() {
		return keyId;
	}

	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}
}
