package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class DetailSourceSwitching implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4795632694832076271L;
	private String lji_id;
	private BigDecimal mpt_jumlah;
	private BigDecimal mpt_unit;
	private String mpt_dk;
	private Integer mpt_persen;
	ArrayList<DetailDestinationSwitching> destination;

	public ArrayList<DetailDestinationSwitching> getDestination() {
		return destination;
	}

	public void setDestination(ArrayList<DetailDestinationSwitching> destination) {
		this.destination = destination;
	}

	public String getLji_id() {
		return lji_id;
	}

	public BigDecimal getMpt_jumlah() {
		return mpt_jumlah;
	}

	public BigDecimal getMpt_unit() {
		return mpt_unit;
	}

	public String getMpt_dk() {
		return mpt_dk;
	}

	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}

	public void setMpt_jumlah(BigDecimal mpt_jumlah) {
		this.mpt_jumlah = mpt_jumlah;
	}

	public void setMpt_unit(BigDecimal mpt_unit) {
		this.mpt_unit = mpt_unit;
	}

	public void setMpt_dk(String mpt_dk) {
		this.mpt_dk = mpt_dk;
	}

	public Integer getMpt_persen() {
		return mpt_persen;
	}

	public void setMpt_persen(Integer mpt_persen) {
		this.mpt_persen = mpt_persen;
	}
}