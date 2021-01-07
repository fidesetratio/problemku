package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Fund implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8878513366837071402L;
	private String lji_id;
	private String lji_invest;
	private String fund_id;
	private String fund_invest;
	private String lku_symbol;
	private BigDecimal mdu_persen;

	public String getLji_id() {
		return lji_id;
	}

	public String getLji_invest() {
		return lji_invest;
	}

	public String getFund_id() {
		return fund_id;
	}

	public String getFund_invest() {
		return fund_invest;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public BigDecimal getMdu_persen() {
		return mdu_persen;
	}

	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}

	public void setLji_invest(String lji_invest) {
		this.lji_invest = lji_invest;
	}

	public void setFund_id(String fund_id) {
		this.fund_id = fund_id;
	}

	public void setFund_invest(String fund_invest) {
		this.fund_invest = fund_invest;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public void setMdu_persen(BigDecimal mdu_persen) {
		this.mdu_persen = mdu_persen;
	}
}