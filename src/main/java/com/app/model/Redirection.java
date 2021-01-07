package com.app.model;

import java.io.Serializable;

public class Redirection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7169213562553753747L;
	private String mpt_id_redirection;
	private String lku_id;
	private String payor_name;
	private DetailRedirection detail_redirection = null;

	public String getMpt_id_redirection() {
		return mpt_id_redirection;
	}

	public String getLku_id() {
		return lku_id;
	}

	public String getPayor_name() {
		return payor_name;
	}

	public DetailRedirection getDetail_redirection() {
		return detail_redirection;
	}

	public void setMpt_id_redirection(String mpt_id_redirection) {
		this.mpt_id_redirection = mpt_id_redirection;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public void setPayor_name(String payor_name) {
		this.payor_name = payor_name;
	}

	public void setDetail_redirection(DetailRedirection detail_redirection) {
		this.detail_redirection = detail_redirection;
	}
}