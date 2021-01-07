package com.app.model.request;

import java.io.Serializable;

public class RequestFindAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7481680793768718556L;
	private String ktp_or_nopolis;
	private String dob;

	public String getKtp_or_nopolis() {
		return ktp_or_nopolis;
	}

	public String getDob() {
		return dob;
	}

	public void setKtp_or_nopolis(String ktp_or_nopolis) {
		this.ktp_or_nopolis = ktp_or_nopolis;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}
}
