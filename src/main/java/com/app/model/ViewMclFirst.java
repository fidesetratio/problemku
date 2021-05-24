package com.app.model;

import java.io.Serializable;

public class ViewMclFirst implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -187876306811723871L;
	private String no_polis;
	private String nama;
	
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
	
}
