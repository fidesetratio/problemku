package com.app.model.request;

import java.io.Serializable;

public class RequestSendOTP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4083123624084050313L;
	private String phone_no;
	public Integer jenis_id;
	public Integer menu_id;
	public String username;
	public String reg_spaj;
	public String no_polis;

	public Integer getJenis_id() {
		return jenis_id;
	}

	public void setJenis_id(Integer jenis_id) {
		this.jenis_id = jenis_id;
	}

	public Integer getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public String getPhone_no() {
		return phone_no;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}
}