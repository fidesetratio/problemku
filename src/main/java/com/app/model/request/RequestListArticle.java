package com.app.model.request;

import java.io.Serializable;

public class RequestListArticle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7495441860935444658L;
	private Integer pageNumber;
	private Integer pageSize;

	public Integer getPageNumber() {
		return pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}