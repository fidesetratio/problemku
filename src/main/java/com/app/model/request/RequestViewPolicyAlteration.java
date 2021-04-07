package com.app.model.request;

import java.io.Serializable;

public class RequestViewPolicyAlteration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5124476028480756969L;
	private String username;
	private String key;
	private String no_polis;
	private String tanggal_awal;
	private String tanggal_akhir;

	public String getTanggal_awal() {
		return tanggal_awal;
	}

	public void setTanggal_awal(String tanggal_awal) {
		this.tanggal_awal = tanggal_awal;
	}

	public String getTanggal_akhir() {
		return tanggal_akhir;
	}

	public void setTanggal_akhir(String tanggal_akhir) {
		this.tanggal_akhir = tanggal_akhir;
	}

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
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

}