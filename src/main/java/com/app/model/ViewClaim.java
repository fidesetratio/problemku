package com.app.model;

import java.io.Serializable;

public class ViewClaim implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -187876306811723871L;
	private String username;
	private String key;
	private String no_polis;
	private Integer pageSize;
	private Integer pageNumber;
	private String spaj;
	private String startDate;
	private String endDate;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public String getSpaj() {
		return spaj;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
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

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setSpaj(String spaj) {
		this.spaj = spaj;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
