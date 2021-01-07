package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PowerSave implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3603123166536880011L;
	private Date beg_date;
	private Date end_date;
	private BigDecimal id;
	private BigDecimal rate;
	private BigDecimal deposit;
	private BigDecimal interest;
	private BigDecimal mpr_jangka_invest;
	private String lku_symbol;
	private String jenis;

	public Date getBeg_date() {
		return beg_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public BigDecimal getId() {
		return id;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public BigDecimal getMpr_jangka_invest() {
		return mpr_jangka_invest;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public String getJenis() {
		return jenis;
	}

	public void setBeg_date(Date beg_date) {
		this.beg_date = beg_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public void setMpr_jangka_invest(BigDecimal mpr_jangka_invest) {
		this.mpr_jangka_invest = mpr_jangka_invest;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public void setJenis(String jenis) {
		this.jenis = jenis;
	}
}
