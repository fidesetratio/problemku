package com.app.model;

import java.io.Serializable;

public class ClaimLimit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2356621085798776512L;	
	private String regclaim;
	private String mspo_policy_no;
	private String reg_spaj;
	private String nama_produk;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private String beg_date;
	private String now;
	private String end_date;
	private String jenis_jaminan;
	private String limit_per;
	private String per_jaminan;
	private String limit;
	private String as_charge;
	private String bayar_klaim;
	private String tgl_akseptasi;
	private String sisa_limit;
	
	
	public String getRegclaim() {
		return regclaim;
	}
	public void setRegclaim(String regclaim) {
		this.regclaim = regclaim;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getNama_produk() {
		return nama_produk;
	}
	public void setNama_produk(String nama_produk) {
		this.nama_produk = nama_produk;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public String getBeg_date() {
		return beg_date;
	}
	public void setBeg_date(String beg_date) {
		this.beg_date = beg_date;
	}
	public String getNow() {
		return now;
	}
	public void setNow(String now) {
		this.now = now;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getJenis_jaminan() {
		return jenis_jaminan;
	}
	public void setJenis_jaminan(String jenis_jaminan) {
		this.jenis_jaminan = jenis_jaminan;
	}
	public String getLimit_per() {
		return limit_per;
	}
	public void setLimit_per(String limit_per) {
		this.limit_per = limit_per;
	}
	public String getPer_jaminan() {
		return per_jaminan;
	}
	public void setPer_jaminan(String per_jaminan) {
		this.per_jaminan = per_jaminan;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getAs_charge() {
		return as_charge;
	}
	public void setAs_charge(String as_charge) {
		this.as_charge = as_charge;
	}
	public String getBayar_klaim() {
		return bayar_klaim;
	}
	public void setBayar_klaim(String bayar_klaim) {
		this.bayar_klaim = bayar_klaim;
	}
	public String getTgl_akseptasi() {
		return tgl_akseptasi;
	}
	public void setTgl_akseptasi(String tgl_akseptasi) {
		this.tgl_akseptasi = tgl_akseptasi;
	}
	public String getSisa_limit() {
		return sisa_limit;
	}
	public void setSisa_limit(String sisa_limit) {
		this.sisa_limit = sisa_limit;
	}
	
	
}