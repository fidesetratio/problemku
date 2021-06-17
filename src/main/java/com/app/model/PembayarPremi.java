package com.app.model;

import java.io.Serializable;

public class PembayarPremi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 943749507312112175L;
	private String hubungan;
	private String nama;
	private String jenis_kelamin;
	private String dob;
	private String alamat;
	private String no_hp;
	private String no_rekening;
	private String cara_bayar;
	
	public String getCara_bayar() {
		return cara_bayar;
	}
	public void setCara_bayar(String cara_bayar) {
		this.cara_bayar = cara_bayar;
	}
	public String getNo_rekening() {
		return no_rekening;
	}
	public void setNo_rekening(String no_rekening) {
		this.no_rekening = no_rekening;
	}
	public String getHubungan() {
		return hubungan;
	}
	public void setHubungan(String hubungan) {
		this.hubungan = hubungan;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getJenis_kelamin() {
		return jenis_kelamin;
	}
	public void setJenis_kelamin(String jenis_kelamin) {
		this.jenis_kelamin = jenis_kelamin;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public String getNo_hp() {
		return no_hp;
	}
	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}
}
