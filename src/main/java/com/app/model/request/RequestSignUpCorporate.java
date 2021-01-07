package com.app.model.request;

import java.io.Serializable;

public class RequestSignUpCorporate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3229102904612131814L;
	private String no_polis;
	private String dob;
	private String kode;

	public String getNo_polis() {
		return no_polis;
	}

	public String getDob() {
		return dob;
	}

	public String getKode() {
		return kode;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}
}