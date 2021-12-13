package com.app.model.request;

import java.io.Serializable;

public class RequestForgotUsername implements Serializable {

	/**
	 * @param type if 1 --> Individu, 2 --> Corporate
	 * @param kode digunakan untuk forgot corporate
	 */
	private static final long serialVersionUID = -5475046463158363137L;
	private Integer type = 1;
	private String ktp_or_nopolis;
	private String account_no_dplk;
	private String dob;
	private String kode;

	public Integer getType() {
		return type;
	}

	public String getKtp_or_nopolis() {
		return ktp_or_nopolis;
	}

	public String getDob() {
		return dob;
	}

	public String getKode() {
		return kode;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setKtp_or_nopolis(String ktp_or_nopolis) {
		this.ktp_or_nopolis = ktp_or_nopolis;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public String getAccount_no_dplk() {
		return account_no_dplk;
	}

	public void setAccount_no_dplk(String account_no_dplk) {
		this.account_no_dplk = account_no_dplk;
	}
}