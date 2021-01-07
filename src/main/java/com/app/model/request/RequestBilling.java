package com.app.model.request;

import java.io.Serializable;

public class RequestBilling implements Serializable {

	/**
	 * @author Muhamad Fajar
	 * @param key: data yang dikirimkan pada saat user login
	 * @param pageNumber: Menentukan nomor halaman yang ditampilkan
	 * @param pageSize: Jumlah data yang keluar pada nomor halaman yang dipilih
	 * @param startDate: format yyyy-mm
	 * @param endDate: format yyyy-mm
	 */
	private static final long serialVersionUID = 5918484930842152817L;
	private String username;
	private String key;
	private String no_polis;
	private Integer pageNumber;
	private Integer pageSize;
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

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}