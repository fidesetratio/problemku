package com.app.model.request;

import java.io.Serializable;

public class RequestDetailStableLink implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1851822437003300157L;
	private String username;
	private String key;
	private String no_polis;
	private String startDate;
	private String endDate;
	private Integer pageNumber;
	private Integer pageSize;
	private Integer msl_tu_ke;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getMsl_tu_ke() {
		return msl_tu_ke;
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

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setMsl_tu_ke(Integer msl_tu_ke) {
		this.msl_tu_ke = msl_tu_ke;
	}
}
