package com.app.model.request;

import java.io.Serializable;
import java.math.BigInteger;

public class RequestViewClaimSubmission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5124476028480756969L;
	private String username;
	private String key;
	private String no_polis;
	private BigInteger mpc_id;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public BigInteger getMpc_id() {
		return mpc_id;
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

	public void setMpc_id(BigInteger mpc_id) {
		this.mpc_id = mpc_id;
	}
}