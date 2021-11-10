package com.app.model;

import io.swagger.models.auth.In;

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

	private String no_va;
	private String lku_symbol;
	private String mspo_policy_no_format;
	private Integer premium_bill_va;
	private Integer premium_bill_transfer;
	private Integer premium_bill_online;
	private Integer premium_bill_bankas_transfer;

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

	public String getNo_va() {
		return no_va;
	}

	public void setNo_va(String no_va) {
		this.no_va = no_va;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}

	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}

	public Integer getPremium_bill_va() {
		return premium_bill_va;
	}

	public void setPremium_bill_va(Integer premium_bill_va) {
		this.premium_bill_va = premium_bill_va;
	}

	public Integer getPremium_bill_transfer() {
		return premium_bill_transfer;
	}

	public void setPremium_bill_transfer(Integer premium_bill_transfer) {
		this.premium_bill_transfer = premium_bill_transfer;
	}

	public Integer getPremium_bill_online() {
		return premium_bill_online;
	}

	public void setPremium_bill_online(Integer premium_bill_online) {
		this.premium_bill_online = premium_bill_online;
	}

	public Integer getPremium_bill_bankas_transfer() {
		return premium_bill_bankas_transfer;
	}

	public void setPremium_bill_bankas_transfer(Integer premium_bill_bankas_transfer) {
		this.premium_bill_bankas_transfer = premium_bill_bankas_transfer;
	}

}