package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StableSave implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2531220835935021339L;
	private BigDecimal id;
	private Date mss_bdate;
	private Date mss_edate;
	private BigDecimal mss_mgi;
	private BigDecimal mss_rate;
	private BigDecimal deposit;
	private BigDecimal mss_bunga;
	private String lku_symbol;
	private String transactions;

	public BigDecimal getId() {
		return id;
	}

	public Date getMss_bdate() {
		return mss_bdate;
	}

	public Date getMss_edate() {
		return mss_edate;
	}

	public BigDecimal getMss_mgi() {
		return mss_mgi;
	}

	public BigDecimal getMss_rate() {
		return mss_rate;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public BigDecimal getMss_bunga() {
		return mss_bunga;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public String getTransactions() {
		return transactions;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public void setMss_bdate(Date mss_bdate) {
		this.mss_bdate = mss_bdate;
	}

	public void setMss_edate(Date mss_edate) {
		this.mss_edate = mss_edate;
	}

	public void setMss_mgi(BigDecimal mss_mgi) {
		this.mss_mgi = mss_mgi;
	}

	public void setMss_rate(BigDecimal mss_rate) {
		this.mss_rate = mss_rate;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	public void setMss_bunga(BigDecimal mss_bunga) {
		this.mss_bunga = mss_bunga;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public void setTransactions(String transactions) {
		this.transactions = transactions;
	}
}
