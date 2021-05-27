package com.app.model.request;

import java.io.Serializable;

public class RequestReportHr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6844831461013071962L;
	private String username;
	private String key;
	private String no_polis;
	private String no_batch;
	private String beg_date;
	private String end_date;
	private Integer pageNumber;
	private Integer pageSize;
	
	public String getBeg_date() {
		return beg_date;
	}

	public void setBeg_date(String beg_date) {
		this.beg_date = beg_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getNo_batch() {
		return no_batch;
	}

	public void setNo_batch(String no_batch) {
		this.no_batch = no_batch;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
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
}
