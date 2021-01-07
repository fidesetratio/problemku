package com.app.model.request;

import java.io.Serializable;

public class RequestRegisterQR implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3166467962189698831L;
	private String username;
	private String password;
	private String no_polis;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}
}
