package com.app.model.request;

import java.io.Serializable;

public class RequestValidateOTP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2330852503871931676L;
	private String phone_no;
	public Integer jenis_id;
	public Integer menu_id;
	public String username;
	public Integer otp_no;
	public Integer otp_number;

	public Integer getOtp_number() {
		return otp_number;
	}

	public void setOtp_number(Integer otp_number) {
		this.otp_number = otp_number;
	}

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

	public Integer getOtp_no() {
		return otp_no;
	}

	public void setOtp_no(Integer otp_no) {
		this.otp_no = otp_no;
	}
	
	public String getPhone_no() {
		return phone_no;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

}
