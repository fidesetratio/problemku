package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class KlaimKesehatan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5205360037936323060L;
	private Date regapldate;
	private Date clm_paid_date;
	private String jenis_claim;
	private String nm_diagnos;
	private String lku_symbol;
	private BigDecimal amt_claim;
	private BigDecimal pay_claim;
	private BigDecimal id_status_accept;

	public Date getRegapldate() {
		return regapldate;
	}

	public void setRegapldate(Date regapldate) {
		this.regapldate = regapldate;
	}

	public Date getClm_paid_date() {
		return clm_paid_date;
	}

	public void setClm_paid_date(Date clm_paid_date) {
		this.clm_paid_date = clm_paid_date;
	}

	public String getJenis_claim() {
		return jenis_claim;
	}

	public void setJenis_claim(String jenis_claim) {
		this.jenis_claim = jenis_claim;
	}

	public String getNm_diagnos() {
		return nm_diagnos;
	}

	public void setNm_diagnos(String nm_diagnos) {
		this.nm_diagnos = nm_diagnos;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public BigDecimal getAmt_claim() {
		return amt_claim;
	}

	public void setAmt_claim(BigDecimal amt_claim) {
		this.amt_claim = amt_claim;
	}

	public BigDecimal getPay_claim() {
		return pay_claim;
	}

	public void setPay_claim(BigDecimal pay_claim) {
		this.pay_claim = pay_claim;
	}

	public BigDecimal getId_status_accept() {
		return id_status_accept;
	}

	public void setId_status_accept(BigDecimal id_status_accept) {
		this.id_status_accept = id_status_accept;
	}
}
