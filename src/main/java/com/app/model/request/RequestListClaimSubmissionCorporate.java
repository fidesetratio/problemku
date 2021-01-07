package com.app.model.request;

import java.io.Serializable;

public class RequestListClaimSubmissionCorporate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 618493791406936933L;
	private String username;
	private String key;
	private String reg_spaj;
	private String mste_insured;
	private Integer pageNumber;
	private Integer pageSize;

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

	public Integer getPageNumber() {
		return pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
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

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}