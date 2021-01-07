package com.app.model;

import java.io.Serializable;

public class TertanggungTambahan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7677266255626285831L;
	private String nama;
	private String lsre_relation;
	private String tgl_lahir;
	private String kelamin;

	public String getNama() {
		return nama;
	}

	public String getLsre_relation() {
		return lsre_relation;
	}

	public String getTgl_lahir() {
		return tgl_lahir;
	}

	public String getKelamin() {
		return kelamin;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public void setLsre_relation(String lsre_relation) {
		this.lsre_relation = lsre_relation;
	}

	public void setTgl_lahir(String tgl_lahir) {
		this.tgl_lahir = tgl_lahir;
	}

	public void setKelamin(String kelamin) {
		this.kelamin = kelamin;
	}
}
