package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Switching implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1096670708156270403L;
	private String mpt_id_switching;
	private String lku_id;
	private BigDecimal mpt_jumlah;
	private BigDecimal mpt_unit;
	private String payor_name;
	private DetailSwitching detail_switching = null;
	private ArrayList<String> lji_id;

	public String getMpt_id_switching() {
		return mpt_id_switching;
	}

	public String getLku_id() {
		return lku_id;
	}

	public BigDecimal getMpt_jumlah() {
		return mpt_jumlah;
	}

	public BigDecimal getMpt_unit() {
		return mpt_unit;
	}

	public String getPayor_name() {
		return payor_name;
	}

	public DetailSwitching getDetail_switching() {
		return detail_switching;
	}

	public void setMpt_id_switching(String mpt_id_switching) {
		this.mpt_id_switching = mpt_id_switching;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public void setMpt_jumlah(BigDecimal mpt_jumlah) {
		this.mpt_jumlah = mpt_jumlah;
	}

	public void setMpt_unit(BigDecimal mpt_unit) {
		this.mpt_unit = mpt_unit;
	}

	public void setPayor_name(String payor_name) {
		this.payor_name = payor_name;
	}

	public void setDetail_switching(DetailSwitching detail_switching) {
		this.detail_switching = detail_switching;
	}

	public ArrayList<String> getLji_id() {
		return lji_id;
	}

	public void setLji_id(ArrayList<String> lji_id) {
		this.lji_id = lji_id;
	}
}