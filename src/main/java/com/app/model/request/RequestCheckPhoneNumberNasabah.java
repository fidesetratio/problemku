package com.app.model.request;

import java.io.Serializable;

public class RequestCheckPhoneNumberNasabah implements Serializable {
	/**
	 * @param: no_polis --> Individu
	 * @param: reg_spaj, mste_insured --> Corporate
	 * @param: type --> 1: Individu, 2: Corporate
	 */
	private static final long serialVersionUID = -5319574235354351605L;
	private String username;
	private String key;
	private Integer type;
	private String no_polis;
	private String reg_spaj;
	private String mste_insured;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public Integer getType() {
		return type;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public String getMste_insured() {
		return mste_insured;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public void setMste_insured(String mste_insured) {
		this.mste_insured = mste_insured;
	}
}
