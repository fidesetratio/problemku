package com.app.model.request;

import java.io.Serializable;

public class RequestValidateOTP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2330852503871931676L;
	private String phone_no;
	private Integer otp_no;
	private Integer menu_id = 1;

	public String getPhone_no() {
		return phone_no;
	}

	public Integer getOtp_no() {
		return otp_no;
	}

	public Integer getMenu_id() {
		return menu_id;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	public void setOtp_no(Integer otp_no) {
		this.otp_no = otp_no;
	}

	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}
}
