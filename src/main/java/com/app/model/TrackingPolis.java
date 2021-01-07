package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class TrackingPolis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3444198185892209443L;
	private String mspo_policy_no;
	private Date msps_date;
	private String lspd_position;
	private String lssp_status;
	private String status_accept;
	private String msps_desc;
	private String reg_spaj;

	public String getMspo_policy_no() {
		return mspo_policy_no;
	}

	public Date getMsps_date() {
		return msps_date;
	}

	public String getLspd_position() {
		return lspd_position;
	}

	public String getLssp_status() {
		return lssp_status;
	}

	public String getStatus_accept() {
		return status_accept;
	}

	public String getMsps_desc() {
		return msps_desc;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}

	public void setMsps_date(Date msps_date) {
		this.msps_date = msps_date;
	}

	public void setLspd_position(String lspd_position) {
		this.lspd_position = lspd_position;
	}

	public void setLssp_status(String lssp_status) {
		this.lssp_status = lssp_status;
	}

	public void setStatus_accept(String status_accept) {
		this.status_accept = status_accept;
	}

	public void setMsps_desc(String msps_desc) {
		this.msps_desc = msps_desc;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
}