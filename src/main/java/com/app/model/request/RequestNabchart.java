package com.app.model.request;

import java.io.Serializable;

public class RequestNabchart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1137394804700420127L;
	private String username;
	private String key;
	private Integer id;
	private Integer nilai;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public Integer getId() {
		return id;
	}

	public Integer getNilai() {
		return nilai;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNilai(Integer nilai) {
		this.nilai = nilai;
	}
}
