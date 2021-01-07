package com.app.model.request;

import java.io.Serializable;

public class RequestDetailClaimCorporate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -819165231286988367L;
	private String username;
	private String key;
	private String no_claim;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_claim() {
		return no_claim;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setNo_claim(String no_claim) {
		this.no_claim = no_claim;
	}
}
