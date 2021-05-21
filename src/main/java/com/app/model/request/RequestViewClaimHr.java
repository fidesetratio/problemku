package com.app.model.request;

import java.io.Serializable;

public class RequestViewClaimHr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5679664754322258278L;
	private String username;
	private String key;
	private String reg_spaj;
	private String mste_insured;
	private String mpcc_id;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public String getMste_insured() {
		return mste_insured;
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

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public void setMste_insured(String mste_insured) {
		this.mste_insured = mste_insured;
	}

	public void setMpcc_id(String mpcc_id) {
		this.mpcc_id = mpcc_id;
	}
}