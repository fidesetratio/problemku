package com.app.model.request;

import java.io.Serializable;

public class RequestDetailInvestasiTransaksi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7986379781312280373L;
	private String username;
	private String key;
	private String no_polis;
	private String startDate;
	private String endDate;
	private Integer pageNumber;
	private Integer pageSize;
	private String lji_id;

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

	public String getLji_id() {
		return lji_id;
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

	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}
}
