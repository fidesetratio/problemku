package com.app.model.request;

import java.io.Serializable;

public class RequestListPolis implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5619732625357507168L;
	private String username;
	private String key;
	private String search_policy;
	private Integer search_type;
	private String policy_no;
	private String mste_insured;
	private String reg_spaj;

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public String getPolicy_no() {
		return policy_no;
	}

	public void setPolicy_no(String policy_no) {
		this.policy_no = policy_no;
	}

	public String getSearch_policy() {
		return search_policy;
	}

	public void setSearch_policy(String search_policy) {
		this.search_policy = search_policy;
	}

	public Integer getSearch_type() {
		return search_type;
	}

	public void setSearch_type(Integer search_type) {
		this.search_type = search_type;
	}

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMste_insured() {
		return mste_insured;
	}

	public void setMste_insured(String mste_insured) {
		this.mste_insured = mste_insured;
	}
}
