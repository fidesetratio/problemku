package com.app.model.request;

import java.io.Serializable;

public class RequestForgotPassword implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8973499884484171317L;
	private String username;
	private Integer menu_id = 1;

	public String getUsername() {
		return username;
	}

	public Integer getMenu_id() {
		return menu_id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}
}
