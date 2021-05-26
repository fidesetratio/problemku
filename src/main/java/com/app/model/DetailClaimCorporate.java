package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class DetailClaimCorporate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8455351811048588652L;
	private String detail;
	private BigDecimal jml_klaim;
	private BigDecimal jml_dibayar;
	private String tgl_input;
	private String mbc_no;
	private String mce_klaim_admedika;
	
	public String getTgl_input() {
		return tgl_input;
	}

	public void setTgl_input(String tgl_input) {
		this.tgl_input = tgl_input;
	}

	public String getMbc_no() {
		return mbc_no;
	}

	public void setMbc_no(String mbc_no) {
		this.mbc_no = mbc_no;
	}

	public String getMce_klaim_admedika() {
		return mce_klaim_admedika;
	}

	public void setMce_klaim_admedika(String mce_klaim_admedika) {
		this.mce_klaim_admedika = mce_klaim_admedika;
	}

	public String getDetail() {
		return detail;
	}

	public BigDecimal getJml_klaim() {
		return jml_klaim;
	}

	public BigDecimal getJml_dibayar() {
		return jml_dibayar;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setJml_klaim(BigDecimal jml_klaim) {
		this.jml_klaim = jml_klaim;
	}

	public void setJml_dibayar(BigDecimal jml_dibayar) {
		this.jml_dibayar = jml_dibayar;
	}
}
