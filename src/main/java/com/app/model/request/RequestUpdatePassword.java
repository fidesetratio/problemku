package com.app.model.request;

import java.io.Serializable;

public class RequestUpdatePassword implements Serializable {

	/**
	 * @param typeUpdatePassword 
	 * 1: Tidak update ke activity user di table eka.lst_user_simultaneous
	 * 2: Update ke activity user di table eka.lst_user_simultaneous
	 */
	private static final long serialVersionUID = -722571991665116860L;
	private String username = null;
	private String new_password;
	private String phone_no;
	private Integer typeUpdatePassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNew_password() {
		return new_password;
	}

	public String getPhone_no() {
		return phone_no;
	}

	public Integer getTypeUpdatePassword() {
		return typeUpdatePassword;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	public void setTypeUpdatePassword(Integer typeUpdatePassword) {
		this.typeUpdatePassword = typeUpdatePassword;
	}
}
