package com.app.model.request;

import java.io.Serializable;

public class RequestLinkAccount implements Serializable {

	/**
	 * @param: type_register: 1. Individual 2. Corporate
	 */
	private static final long serialVersionUID = 9078989165929873531L;
	public Integer type_register;
	public String username;
	public String no_polis;
	public String mcl_id_employee;
	public String account_no_dplk;

	public Integer getType_register() {
		return type_register;
	}

	public String getUsername() {
		return username;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public String getMcl_id_employee() {
		return mcl_id_employee;
	}

	public void setType_register(Integer type_register) {
		this.type_register = type_register;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public void setMcl_id_employee(String mcl_id_employee) {
		this.mcl_id_employee = mcl_id_employee;
	}

	public String getAccount_no_dplk() {
		return account_no_dplk;
	}

	public void setAccount_no_dplk(String account_no_dplk) {
		this.account_no_dplk = account_no_dplk;
	}
}
