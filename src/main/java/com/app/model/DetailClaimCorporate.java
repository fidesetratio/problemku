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
