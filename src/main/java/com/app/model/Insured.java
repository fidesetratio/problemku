package com.app.model;

import java.io.Serializable;

public class Insured implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2356621085798776512L;	
	private DetailPolicyAlteration status_tt;
	private DetailPolicyAlteration agama_tt;
	private DetailPolicyAlteration kewarganegaraan_tt;
	private DetailPolicyAlteration nama_perusahaan_tt;
	private DetailPolicyAlteration jabatan_tt;
	private DetailPolicyAlteration tipe_usaha_tt;
	
	public DetailPolicyAlteration getStatus_tt() {
		return status_tt;
	}
	public void setStatus_tt(DetailPolicyAlteration status_tt) {
		this.status_tt = status_tt;
	}
	public DetailPolicyAlteration getAgama_tt() {
		return agama_tt;
	}
	public void setAgama_tt(DetailPolicyAlteration agama_tt) {
		this.agama_tt = agama_tt;
	}
	public DetailPolicyAlteration getKewarganegaraan_tt() {
		return kewarganegaraan_tt;
	}
	public void setKewarganegaraan_tt(DetailPolicyAlteration kewarganegaraan_tt) {
		this.kewarganegaraan_tt = kewarganegaraan_tt;
	}
	public DetailPolicyAlteration getNama_perusahaan_tt() {
		return nama_perusahaan_tt;
	}
	public void setNama_perusahaan_tt(DetailPolicyAlteration nama_perusahaan_tt) {
		this.nama_perusahaan_tt = nama_perusahaan_tt;
	}
	public DetailPolicyAlteration getJabatan_tt() {
		return jabatan_tt;
	}
	public void setJabatan_tt(DetailPolicyAlteration jabatan_tt) {
		this.jabatan_tt = jabatan_tt;
	}
	public DetailPolicyAlteration getTipe_usaha_tt() {
		return tipe_usaha_tt;
	}
	public void setTipe_usaha_tt(DetailPolicyAlteration tipe_usaha_tt) {
		this.tipe_usaha_tt = tipe_usaha_tt;
	}
}