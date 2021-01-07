package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StableLink implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4519739013688018033L;
	private BigDecimal msl_tu_ke;
	private String msl_desc;
	private BigDecimal msl_kode;
	private BigDecimal msl_unit;
	private BigDecimal msl_nab;
	private BigDecimal jumlah_premi_bunga;
	private String lku_symbol;
	private BigDecimal id;
	private Date msl_bdate;
	private Date msl_edate;
	private BigDecimal msl_premi;
	private BigDecimal msl_bunga;
	private BigDecimal msl_rate;
	private BigDecimal msl_nilai_polis;
	private BigDecimal msl_mgi;
	private String jenis;

	public BigDecimal getMsl_tu_ke() {
		return msl_tu_ke;
	}

	public String getMsl_desc() {
		return msl_desc;
	}

	public BigDecimal getMsl_kode() {
		return msl_kode;
	}

	public BigDecimal getMsl_unit() {
		return msl_unit;
	}

	public BigDecimal getMsl_nab() {
		return msl_nab;
	}

	public BigDecimal getJumlah_premi_bunga() {
		return jumlah_premi_bunga;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public BigDecimal getId() {
		return id;
	}

	public Date getMsl_bdate() {
		return msl_bdate;
	}

	public Date getMsl_edate() {
		return msl_edate;
	}

	public BigDecimal getMsl_premi() {
		return msl_premi;
	}

	public BigDecimal getMsl_bunga() {
		return msl_bunga;
	}

	public BigDecimal getMsl_rate() {
		return msl_rate;
	}

	public BigDecimal getMsl_nilai_polis() {
		return msl_nilai_polis;
	}

	public BigDecimal getMsl_mgi() {
		return msl_mgi;
	}

	public String getJenis() {
		return jenis;
	}

	public void setMsl_tu_ke(BigDecimal msl_tu_ke) {
		this.msl_tu_ke = msl_tu_ke;
	}

	public void setMsl_desc(String msl_desc) {
		this.msl_desc = msl_desc;
	}

	public void setMsl_kode(BigDecimal msl_kode) {
		this.msl_kode = msl_kode;
	}

	public void setMsl_unit(BigDecimal msl_unit) {
		this.msl_unit = msl_unit;
	}

	public void setMsl_nab(BigDecimal msl_nab) {
		this.msl_nab = msl_nab;
	}

	public void setJumlah_premi_bunga(BigDecimal jumlah_premi_bunga) {
		this.jumlah_premi_bunga = jumlah_premi_bunga;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public void setMsl_bdate(Date msl_bdate) {
		this.msl_bdate = msl_bdate;
	}

	public void setMsl_edate(Date msl_edate) {
		this.msl_edate = msl_edate;
	}

	public void setMsl_premi(BigDecimal msl_premi) {
		this.msl_premi = msl_premi;
	}

	public void setMsl_bunga(BigDecimal msl_bunga) {
		this.msl_bunga = msl_bunga;
	}

	public void setMsl_rate(BigDecimal msl_rate) {
		this.msl_rate = msl_rate;
	}

	public void setMsl_nilai_polis(BigDecimal msl_nilai_polis) {
		this.msl_nilai_polis = msl_nilai_polis;
	}

	public void setMsl_mgi(BigDecimal msl_mgi) {
		this.msl_mgi = msl_mgi;
	}

	public void setJenis(String jenis) {
		this.jenis = jenis;
	}
}
