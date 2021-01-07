package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class DetailWithdraw implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5134496747788865781L;
	private String lji_id;
	private String lji_invest;
	private BigDecimal mpt_jumlah_detail;
	private BigDecimal mpt_unit_detail;
	private BigDecimal nav_process;
	private String lku_symbol;

	public String getLji_id() {
		return lji_id;
	}

	public String getLji_invest() {
		return lji_invest;
	}

	public BigDecimal getMpt_jumlah_detail() {
		return mpt_jumlah_detail;
	}

	public BigDecimal getMpt_unit_detail() {
		return mpt_unit_detail;
	}

	public BigDecimal getNav_process() {
		return nav_process;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}

	public void setLji_invest(String lji_invest) {
		this.lji_invest = lji_invest;
	}

	public void setMpt_jumlah_detail(BigDecimal mpt_jumlah_detail) {
		this.mpt_jumlah_detail = mpt_jumlah_detail;
	}

	public void setMpt_unit_detail(BigDecimal mpt_unit_detail) {
		this.mpt_unit_detail = mpt_unit_detail;
	}

	public void setNav_process(BigDecimal nav_process) {
		this.nav_process = nav_process;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
}