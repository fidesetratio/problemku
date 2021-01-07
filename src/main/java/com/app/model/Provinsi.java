package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Provinsi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8216231010073285032L;
	private Integer requestCode;
	private BigDecimal lspr_id;
	private BigDecimal lska_id;
	private BigDecimal lskc_id;
	private BigDecimal lskl_id;
	private BigDecimal kodepos;
	private String username;
	private String propinsi;
	private String kabupaten;
	private String kecamatan;
	private String kelurahan;
	private String key;

	public Integer getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(Integer requestCode) {
		this.requestCode = requestCode;
	}

	public BigDecimal getLspr_id() {
		return lspr_id;
	}

	public void setLspr_id(BigDecimal lspr_id) {
		this.lspr_id = lspr_id;
	}

	public BigDecimal getLska_id() {
		return lska_id;
	}

	public void setLska_id(BigDecimal lska_id) {
		this.lska_id = lska_id;
	}

	public BigDecimal getLskc_id() {
		return lskc_id;
	}

	public void setLskc_id(BigDecimal lskc_id) {
		this.lskc_id = lskc_id;
	}

	public BigDecimal getLskl_id() {
		return lskl_id;
	}

	public void setLskl_id(BigDecimal lskl_id) {
		this.lskl_id = lskl_id;
	}

	public BigDecimal getKodepos() {
		return kodepos;
	}

	public void setKodepos(BigDecimal kodepos) {
		this.kodepos = kodepos;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPropinsi() {
		return propinsi;
	}

	public void setPropinsi(String propinsi) {
		this.propinsi = propinsi;
	}

	public String getKabupaten() {
		return kabupaten;
	}

	public void setKabupaten(String kabupaten) {
		this.kabupaten = kabupaten;
	}

	public String getKecamatan() {
		return kecamatan;
	}

	public void setKecamatan(String kecamatan) {
		this.kecamatan = kecamatan;
	}

	public String getKelurahan() {
		return kelurahan;
	}

	public void setKelurahan(String kelurahan) {
		this.kelurahan = kelurahan;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}