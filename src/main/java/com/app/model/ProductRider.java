package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductRider implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8056766647973459729L;
	private String lsdbs_name;
	private String mspr_beg_date;
	private String mspr_end_date;
	private BigDecimal mspr_tsi;
	private String newname;
	private String lku_symbol;

	public String getLsdbs_name() {
		return lsdbs_name;
	}

	public String getMspr_beg_date() {
		return mspr_beg_date;
	}

	public String getMspr_end_date() {
		return mspr_end_date;
	}

	public BigDecimal getMspr_tsi() {
		return mspr_tsi;
	}

	public String getNewname() {
		return newname;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}

	public void setMspr_beg_date(String mspr_beg_date) {
		this.mspr_beg_date = mspr_beg_date;
	}

	public void setMspr_end_date(String mspr_end_date) {
		this.mspr_end_date = mspr_end_date;
	}

	public void setMspr_tsi(BigDecimal mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}

	public void setNewname(String newname) {
		this.newname = newname;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
}