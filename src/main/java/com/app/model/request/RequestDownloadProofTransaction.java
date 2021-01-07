package com.app.model.request;

import java.io.Serializable;

public class RequestDownloadProofTransaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -240034397815750234L;
	private String username;
	private String key;
	private String no_polis;
	private String mpt_id;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public String getMpt_id() {
		return mpt_id;
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

	public void setMpt_id(String mpt_id) {
		this.mpt_id = mpt_id;
	}
}
