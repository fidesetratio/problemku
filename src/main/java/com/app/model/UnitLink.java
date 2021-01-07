package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UnitLink implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2696382731698600421L;
	private String lji_id;
	private String lji_invest;
	private Date lnu_tgl;
	private BigDecimal harga_Unit;
	private BigDecimal nilai_polis;
	private BigDecimal total_Unit;
	private String lku_symbol;
	private BigDecimal mtu_unit;
	private BigDecimal mtu_nab;
	private String mtu_tgl_nab;
	private String mtu_desc;
	private BigDecimal mtu_jumlah;

	public String getLji_id() {
		return lji_id;
	}

	public String getLji_invest() {
		return lji_invest;
	}

	public Date getLnu_tgl() {
		return lnu_tgl;
	}

	public BigDecimal getHarga_Unit() {
		return harga_Unit;
	}

	public BigDecimal getNilai_polis() {
		return nilai_polis;
	}

	public BigDecimal getTotal_Unit() {
		return total_Unit;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public BigDecimal getMtu_unit() {
		return mtu_unit;
	}

	public BigDecimal getMtu_nab() {
		return mtu_nab;
	}

	public String getMtu_tgl_nab() {
		return mtu_tgl_nab;
	}

	public String getMtu_desc() {
		return mtu_desc;
	}

	public BigDecimal getMtu_jumlah() {
		return mtu_jumlah;
	}

	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}

	public void setLji_invest(String lji_invest) {
		this.lji_invest = lji_invest;
	}

	public void setLnu_tgl(Date lnu_tgl) {
		this.lnu_tgl = lnu_tgl;
	}

	public void setHarga_Unit(BigDecimal harga_Unit) {
		this.harga_Unit = harga_Unit;
	}

	public void setNilai_polis(BigDecimal nilai_polis) {
		this.nilai_polis = nilai_polis;
	}

	public void setTotal_Unit(BigDecimal total_Unit) {
		this.total_Unit = total_Unit;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public void setMtu_unit(BigDecimal mtu_unit) {
		this.mtu_unit = mtu_unit;
	}

	public void setMtu_nab(BigDecimal mtu_nab) {
		this.mtu_nab = mtu_nab;
	}

	public void setMtu_tgl_nab(String mtu_tgl_nab) {
		this.mtu_tgl_nab = mtu_tgl_nab;
	}

	public void setMtu_desc(String mtu_desc) {
		this.mtu_desc = mtu_desc;
	}

	public void setMtu_jumlah(BigDecimal mtu_jumlah) {
		this.mtu_jumlah = mtu_jumlah;
	}
}
