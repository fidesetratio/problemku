package com.app.model.request;

import java.io.Serializable;

public class RequestSwitchingRedirection implements Serializable {

	/**
	 * @param type = 1: Switching 2: Redirection
	 */
	private static final long serialVersionUID = 6584616173149954504L;
	private String username;
	private String key;
	private String no_polis;
	private Integer type;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public Integer getType() {
		return type;
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

	public void setType(Integer type) {
		this.type = type;
	}
}