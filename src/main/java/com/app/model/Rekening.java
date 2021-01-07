package com.app.model;

import java.io.Serializable;

public class Rekening implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9206363497095826551L;
	private String spaj;
	private String bank;
	private String cabang;
	private String atas_nama;
	private String rekening;
	private String kota;

	// Individu
	private String mrc_no_ac;
	private String lsbp_nama;

	// Corporate
	private String msbc_bank;
	private String msbc_acc_no;

	public String getSpaj() {
		return spaj;
	}

	public String getBank() {
		return bank;
	}

	public String getCabang() {
		return cabang;
	}

	public String getAtas_nama() {
		return atas_nama;
	}

	public String getRekening() {
		return rekening;
	}

	public String getKota() {
		return kota;
	}

	public String getMrc_no_ac() {
		return mrc_no_ac;
	}

	public String getLsbp_nama() {
		return lsbp_nama;
	}

	public String getMsbc_bank() {
		return msbc_bank;
	}

	public String getMsbc_acc_no() {
		return msbc_acc_no;
	}

	public void setSpaj(String spaj) {
		this.spaj = spaj;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public void setCabang(String cabang) {
		this.cabang = cabang;
	}

	public void setAtas_nama(String atas_nama) {
		this.atas_nama = atas_nama;
	}

	public void setRekening(String rekening) {
		this.rekening = rekening;
	}

	public void setKota(String kota) {
		this.kota = kota;
	}

	public void setMrc_no_ac(String mrc_no_ac) {
		this.mrc_no_ac = mrc_no_ac;
	}

	public void setLsbp_nama(String lsbp_nama) {
		this.lsbp_nama = lsbp_nama;
	}

	public void setMsbc_bank(String msbc_bank) {
		this.msbc_bank = msbc_bank;
	}

	public void setMsbc_acc_no(String msbc_acc_no) {
		this.msbc_acc_no = msbc_acc_no;
	}
}