package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Provider implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6348319511534595364L;
	private BigDecimal lska_id;
	private String lska_note;
	private BigDecimal rsid;
	private String rsnama;
	private String rsalamat;
	private String rstelepon;
	private BigDecimal rstype;
	private String mappos;
	private String rs_type_label;
	private String type_rs;

	public BigDecimal getLska_id() {
		return lska_id;
	}

	public String getLska_note() {
		return lska_note;
	}

	public BigDecimal getRsid() {
		return rsid;
	}

	public String getRsnama() {
		return rsnama;
	}

	public String getRsalamat() {
		return rsalamat;
	}

	public String getRstelepon() {
		return rstelepon;
	}

	public BigDecimal getRstype() {
		return rstype;
	}

	public String getMappos() {
		return mappos;
	}

	public String getRs_type_label() {
		return rs_type_label;
	}

	public String getType_rs() {
		return type_rs;
	}

	public void setLska_id(BigDecimal lska_id) {
		this.lska_id = lska_id;
	}

	public void setLska_note(String lska_note) {
		this.lska_note = lska_note;
	}

	public void setRsid(BigDecimal rsid) {
		this.rsid = rsid;
	}

	public void setRsnama(String rsnama) {
		this.rsnama = rsnama;
	}

	public void setRsalamat(String rsalamat) {
		this.rsalamat = rsalamat;
	}

	public void setRstelepon(String rstelepon) {
		this.rstelepon = rstelepon;
	}

	public void setRstype(BigDecimal rstype) {
		this.rstype = rstype;
	}

	public void setMappos(String mappos) {
		this.mappos = mappos;
	}

	public void setRs_type_label(String rs_type_label) {
		this.rs_type_label = rs_type_label;
	}

	public void setType_rs(String type_rs) {
		this.type_rs = type_rs;
	}
}
