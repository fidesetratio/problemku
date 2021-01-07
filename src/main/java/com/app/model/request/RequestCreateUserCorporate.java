package com.app.model.request;

import java.io.Serializable;

public class RequestCreateUserCorporate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4661989670937149806L;
	private String username;
	private String password;
	private String mcl_id_employee;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getMcl_id_employee() {
		return mcl_id_employee;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMcl_id_employee(String mcl_id_employee) {
		this.mcl_id_employee = mcl_id_employee;
	}
}