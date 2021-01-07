package com.app.model.request;

import java.io.Serializable;

public class RequestSendOTP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4083123624084050313L;
	private String phone_no;
	private Integer menu_id = 1;
	private String no_polis = null;

	public String getPhone_no() {
		return phone_no;
	}

	public Integer getMenu_id() {
		return menu_id;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}
}