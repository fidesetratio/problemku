package com.app.model;

import java.io.Serializable;

public class ReportHr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8216231010073285032L;
	private String no_batch;
	private String tipe_batch;
	private String tgl_input;
	private String tgl_terima;
	private String tgl_bayar;
	private String mspo_policy_no;
	private String lspd_position;
	private String mbc_kwitansi;
	
	public String getNo_batch() {
		return no_batch;
	}
	public void setNo_batch(String no_batch) {
		this.no_batch = no_batch;
	}
	public String getTipe_batch() {
		return tipe_batch;
	}
	public void setTipe_batch(String tipe_batch) {
		this.tipe_batch = tipe_batch;
	}
	public String getTgl_input() {
		return tgl_input;
	}
	public void setTgl_input(String tgl_input) {
		this.tgl_input = tgl_input;
	}
	public String getTgl_terima() {
		return tgl_terima;
	}
	public void setTgl_terima(String tgl_terima) {
		this.tgl_terima = tgl_terima;
	}
	public String getTgl_bayar() {
		return tgl_bayar;
	}
	public void setTgl_bayar(String tgl_bayar) {
		this.tgl_bayar = tgl_bayar;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public String getLspd_position() {
		return lspd_position;
	}
	public void setLspd_position(String lspd_position) {
		this.lspd_position = lspd_position;
	}
	public String getMbc_kwitansi() {
		return mbc_kwitansi;
	}
	public void setMbc_kwitansi(String mbc_kwitansi) {
		this.mbc_kwitansi = mbc_kwitansi;
	}
}