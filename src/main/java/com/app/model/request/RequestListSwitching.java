package com.app.model.request;

import java.io.Serializable;

public class RequestListSwitching implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3161555969653665396L;
	public String username;
	public String key;
	public String no_polis;
	private Integer pageSize = 12;
	private Integer pageNumber = 1;

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
}