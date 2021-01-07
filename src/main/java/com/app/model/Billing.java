package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Billing implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 326080385768703168L;
	private String paid;
	private String lku_symbol;
	private BigDecimal total_premi;
	private BigDecimal premi_ke;
	private BigDecimal th_ke;
	private Date tgl_bayar;
	private Date periode;

	public String getPaid() {
		return paid;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public BigDecimal getTotal_premi() {
		return total_premi;
	}

	public BigDecimal getPremi_ke() {
		return premi_ke;
	}

	public BigDecimal getTh_ke() {
		return th_ke;
	}

	public Date getTgl_bayar() {
		return tgl_bayar;
	}

	public Date getPeriode() {
		return periode;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public void setTotal_premi(BigDecimal total_premi) {
		this.total_premi = total_premi;
	}

	public void setPremi_ke(BigDecimal premi_ke) {
		this.premi_ke = premi_ke;
	}

	public void setTh_ke(BigDecimal th_ke) {
		this.th_ke = th_ke;
	}

	public void setTgl_bayar(Date tgl_bayar) {
		this.tgl_bayar = tgl_bayar;
	}

	public void setPeriode(Date periode) {
		this.periode = periode;
	}
}