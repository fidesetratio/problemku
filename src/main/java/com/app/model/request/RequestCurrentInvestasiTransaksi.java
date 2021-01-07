package com.app.model.request;

import java.io.Serializable;

public class RequestCurrentInvestasiTransaksi implements Serializable {

	/**
	 * @param status_product:
	 * 1: Power Save
	 * 2: Stable Savbe
	 * 3: Stable Link
	 * 4: Unit Link
	 */
	private static final long serialVersionUID = -3981671961089424037L;
	private String username;
	private String key;
	private String no_polis;
	private String startDate;
	private String endDate;
	private Integer pageNumber;
	private Integer pageSize;
	private Integer status_product;

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

	public Integer getStatus_product() {
		return status_product;
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

	public void setStatus_product(Integer status_product) {
		this.status_product = status_product;
	}
}
