package com.app.model.request;

import java.io.Serializable;

public class RequestFurtherClaimSubmission implements Serializable {

	/**
	 * mpc_id --> Individu
	 * mpcc_id --> Corporate
	 */
	private static final long serialVersionUID = -5697586240600366780L;
	private String username;
	private String key;
	private String mpc_id;
	private String mpcc_id;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getMpc_id() {
		return mpc_id;
	}

	public String getMpcc_id() {
		return mpcc_id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setMpc_id(String mpc_id) {
		this.mpc_id = mpc_id;
	}

	public void setMpcc_id(String mpcc_id) {
		this.mpcc_id = mpcc_id;
	}
}