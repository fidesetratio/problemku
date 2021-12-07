package com.app.model.request;

import io.swagger.models.auth.In;

import java.io.Serializable;

public class RequestTransactionHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7593991121805604265L;
	private String username;
	private String key;
	private String no_polis;
	private String startDate;
	private String endDate;
	private String jenis_transaksi;
	private Integer lt_id;
	private Integer page;
	private Integer size;
	private String kode_transaksi;

	public String getKode_transaksi() {
		return kode_transaksi;
	}

	public void setKode_transaksi(String kode_transaksi) {
		this.kode_transaksi = kode_transaksi;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getLt_id() {
		return lt_id;
	}

	public void setLt_id(Integer lt_id) {
		this.lt_id = lt_id;
	}

	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getJenis_transaksi() {
		return jenis_transaksi;
	}
	public void setJenis_transaksi(String jenis_transaksi) {
		this.jenis_transaksi = jenis_transaksi;
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
	public void setUsername(String username) {
		this.username = username;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}