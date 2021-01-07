package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CostFinancialTransaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9075289874633479977L;
	private String mpt_id;
	private String reg_spaj;
	private Date tgl;
	private BigDecimal lt_id;
	private BigDecimal ljb_id;
	private BigDecimal jumlah;
	private BigDecimal persen;
	private String ljb_biaya;

	public String getMpt_id() {
		return mpt_id;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public Date getTgl() {
		return tgl;
	}

	public BigDecimal getLt_id() {
		return lt_id;
	}

	public BigDecimal getLjb_id() {
		return ljb_id;
	}

	public BigDecimal getJumlah() {
		return jumlah;
	}

	public BigDecimal getPersen() {
		return persen;
	}

	public String getLjb_biaya() {
		return ljb_biaya;
	}

	public void setMpt_id(String mpt_id) {
		this.mpt_id = mpt_id;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public void setTgl(Date tgl) {
		this.tgl = tgl;
	}

	public void setLt_id(BigDecimal lt_id) {
		this.lt_id = lt_id;
	}

	public void setLjb_id(BigDecimal ljb_id) {
		this.ljb_id = ljb_id;
	}

	public void setJumlah(BigDecimal jumlah) {
		this.jumlah = jumlah;
	}

	public void setPersen(BigDecimal persen) {
		this.persen = persen;
	}

	public void setLjb_biaya(String ljb_biaya) {
		this.ljb_biaya = ljb_biaya;
	}
}