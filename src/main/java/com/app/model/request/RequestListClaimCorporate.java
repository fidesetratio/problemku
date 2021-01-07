package com.app.model.request;

import java.io.Serializable;

public class RequestListClaimCorporate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3048982614117364317L;
	private String username;
	private String key;
	private String reg_spaj;
	private String mste_insured;
	private Integer pageSize;
	private Integer pageNumber;

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

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getPageNumber() {
		return pageNumber;
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

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
}
