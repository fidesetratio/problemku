package com.app.model.request;

import java.io.Serializable;

public class RequestTrackingPolis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1599815614079196826L;
	private String no_blanko;
	private String no_polis;
	private String reg_spaj;

	public String getNo_blanko() {
		return no_blanko;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setNo_blanko(String no_blanko) {
		this.no_blanko = no_blanko;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
}
