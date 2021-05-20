package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ReportHr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8216231010073285032L;
	private String reg_spaj;
	private String mspo_policy_no;
	private String mtra_tgl_terima;
	private Integer mtra_no;
	private String mtra_no_terima;
	private String mtra_corporate;
	private String mtra_no_batch;
	private Integer mtra_jml_klm;
	private String mtra_ttl_tagihan;
	private String mtra_nama_peserta;
	private String mtra_ketrangan;
	private String mbc_kwitansi;
	private String mtra_status;
	private Integer mbc_provider;
	
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public String getMtra_tgl_terima() {
		return mtra_tgl_terima;
	}
	public void setMtra_tgl_terima(String mtra_tgl_terima) {
		this.mtra_tgl_terima = mtra_tgl_terima;
	}
	public Integer getMtra_no() {
		return mtra_no;
	}
	public void setMtra_no(Integer mtra_no) {
		this.mtra_no = mtra_no;
	}
	public String getMtra_no_terima() {
		return mtra_no_terima;
	}
	public void setMtra_no_terima(String mtra_no_terima) {
		this.mtra_no_terima = mtra_no_terima;
	}
	public String getMtra_corporate() {
		return mtra_corporate;
	}
	public void setMtra_corporate(String mtra_corporate) {
		this.mtra_corporate = mtra_corporate;
	}
	public String getMtra_no_batch() {
		return mtra_no_batch;
	}
	public void setMtra_no_batch(String mtra_no_batch) {
		this.mtra_no_batch = mtra_no_batch;
	}
	public Integer getMtra_jml_klm() {
		return mtra_jml_klm;
	}
	public void setMtra_jml_klm(Integer mtra_jml_klm) {
		this.mtra_jml_klm = mtra_jml_klm;
	}
	public String getMtra_ttl_tagihan() {
		return mtra_ttl_tagihan;
	}
	public void setMtra_ttl_tagihan(String mtra_ttl_tagihan) {
		this.mtra_ttl_tagihan = mtra_ttl_tagihan;
	}
	public String getMtra_nama_peserta() {
		return mtra_nama_peserta;
	}
	public void setMtra_nama_peserta(String mtra_nama_peserta) {
		this.mtra_nama_peserta = mtra_nama_peserta;
	}
	public String getMtra_ketrangan() {
		return mtra_ketrangan;
	}
	public void setMtra_ketrangan(String mtra_ketrangan) {
		this.mtra_ketrangan = mtra_ketrangan;
	}
	public String getMbc_kwitansi() {
		return mbc_kwitansi;
	}
	public void setMbc_kwitansi(String mbc_kwitansi) {
		this.mbc_kwitansi = mbc_kwitansi;
	}
	public String getMtra_status() {
		return mtra_status;
	}
	public void setMtra_status(String mtra_status) {
		this.mtra_status = mtra_status;
	}
	public Integer getMbc_provider() {
		return mbc_provider;
	}
	public void setMbc_provider(Integer mbc_provider) {
		this.mbc_provider = mbc_provider;
	}
	
	
}