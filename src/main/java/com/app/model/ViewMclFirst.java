package com.app.model;

import java.io.Serializable;

public class ViewMclFirst implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -187876306811723871L;
	private String no_polis;
	private String nama;
	private String nama_tt;
	private Integer policy_status;
	private Integer gprod_id;
	
	public Integer getPolicy_status() {
		return policy_status;
	}
	public void setPolicy_status(Integer policy_status) {
		this.policy_status = policy_status;
	}
	public Integer getGprod_id() {
		return gprod_id;
	}
	public void setGprod_id(Integer gprod_id) {
		this.gprod_id = gprod_id;
	}
	public String getNo_polis() {
		return no_polis;
	}
	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNama_tt() {
		return nama_tt;
	}
	public void setNama_tt(String nama_tt) {
		this.nama_tt = nama_tt;
	}
	
}
