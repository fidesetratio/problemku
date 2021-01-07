package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ClaimCorporate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8130231472403027516L;
	private String no_klaim;
	private Date tgl_rawat;
	private Date tgl_status;
	private Date tgl_klaim;
	private Date tgl_bayar;
	private Date tgl_input;
	private BigDecimal klaim_diajukan;
	private BigDecimal jml_dibayarkan;
	private String status;
	private String nm_plan;
	private String diagnosis;

	public String getNo_klaim() {
		return no_klaim;
	}

	public Date getTgl_rawat() {
		return tgl_rawat;
	}

	public Date getTgl_status() {
		return tgl_status;
	}

	public Date getTgl_klaim() {
		return tgl_klaim;
	}

	public Date getTgl_bayar() {
		return tgl_bayar;
	}

	public Date getTgl_input() {
		return tgl_input;
	}

	public BigDecimal getKlaim_diajukan() {
		return klaim_diajukan;
	}

	public BigDecimal getJml_dibayarkan() {
		return jml_dibayarkan;
	}

	public String getStatus() {
		return status;
	}

	public String getNm_plan() {
		return nm_plan;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setNo_klaim(String no_klaim) {
		this.no_klaim = no_klaim;
	}

	public void setTgl_rawat(Date tgl_rawat) {
		this.tgl_rawat = tgl_rawat;
	}

	public void setTgl_status(Date tgl_status) {
		this.tgl_status = tgl_status;
	}

	public void setTgl_klaim(Date tgl_klaim) {
		this.tgl_klaim = tgl_klaim;
	}

	public void setTgl_bayar(Date tgl_bayar) {
		this.tgl_bayar = tgl_bayar;
	}

	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}

	public void setKlaim_diajukan(BigDecimal klaim_diajukan) {
		this.klaim_diajukan = klaim_diajukan;
	}

	public void setJml_dibayarkan(BigDecimal jml_dibayarkan) {
		this.jml_dibayarkan = jml_dibayarkan;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setNm_plan(String nm_plan) {
		this.nm_plan = nm_plan;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
}