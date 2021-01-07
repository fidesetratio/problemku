package com.app.model.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class RequestCostWithdraw implements Serializable {

	/**
	 * @param proses: 0: Percen 1: Amount 2: Unit
	 */
	private static final long serialVersionUID = 6808538084115343628L;
	private String username;
	private String key;
	private String no_polis;
	private String mpt_id;
	private Integer lt_id;
	private BigDecimal amount;
	private Integer proses;
	private String lku_symbol;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public String getMpt_id() {
		return mpt_id;
	}

	public Integer getLt_id() {
		return lt_id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Integer getProses() {
		return proses;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public void setMpt_id(String mpt_id) {
		this.mpt_id = mpt_id;
	}

	public void setLt_id(Integer lt_id) {
		this.lt_id = lt_id;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setProses(Integer proses) {
		this.proses = proses;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
}