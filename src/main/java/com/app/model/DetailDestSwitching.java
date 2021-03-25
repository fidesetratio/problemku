package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class DetailDestSwitching implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5571045687715024853L;
	private String lji_id_ke;
	private Integer persen_ke;
	private BigDecimal jumlah_ke;
	private BigDecimal unit_ke;
	private String mpt_dk;
	
	public String getLji_id_ke() {
		return lji_id_ke;
	}
	public void setLji_id_ke(String lji_id_ke) {
		this.lji_id_ke = lji_id_ke;
	}
	public Integer getPersen_ke() {
		return persen_ke;
	}
	public void setPersen_ke(Integer persen_ke) {
		this.persen_ke = persen_ke;
	}
	public BigDecimal getJumlah_ke() {
		return jumlah_ke;
	}
	public void setJumlah_ke(BigDecimal jumlah_ke) {
		this.jumlah_ke = jumlah_ke;
	}
	public BigDecimal getUnit_ke() {
		return unit_ke;
	}
	public void setUnit_ke(BigDecimal unit_ke) {
		this.unit_ke = unit_ke;
	}
	public String getMpt_dk() {
		return mpt_dk;
	}
	public void setMpt_dk(String mpt_dk) {
		this.mpt_dk = mpt_dk;
	}

}