package com.app.model.request;

import java.io.Serializable;

public class RequestUpdatePemegangPolis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3569215324189229639L;
	private String username;
	private String key;
	private String no_polis;
	private String alamat_rumah;
	private String email;
	private String kabupaten;
	private Integer lspr_id;
	private Integer lskc_id;
	private Integer lska_id;
	private Integer lskl_id;
	private String kd_pos_rumah;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public String getAlamat_rumah() {
		return alamat_rumah;
	}

	public String getEmail() {
		return email;
	}

	public String getKabupaten() {
		return kabupaten;
	}

	public Integer getLspr_id() {
		return lspr_id;
	}

	public Integer getLskc_id() {
		return lskc_id;
	}

	public Integer getLska_id() {
		return lska_id;
	}

	public Integer getLskl_id() {
		return lskl_id;
	}

	public String getKd_pos_rumah() {
		return kd_pos_rumah;
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

	public void setAlamat_rumah(String alamat_rumah) {
		this.alamat_rumah = alamat_rumah;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setKabupaten(String kabupaten) {
		this.kabupaten = kabupaten;
	}

	public void setLspr_id(Integer lspr_id) {
		this.lspr_id = lspr_id;
	}

	public void setLskc_id(Integer lskc_id) {
		this.lskc_id = lskc_id;
	}

	public void setLska_id(Integer lska_id) {
		this.lska_id = lska_id;
	}

	public void setLskl_id(Integer lskl_id) {
		this.lskl_id = lskl_id;
	}

	public void setKd_pos_rumah(String kd_pos_rumah) {
		this.kd_pos_rumah = kd_pos_rumah;
	}
}
