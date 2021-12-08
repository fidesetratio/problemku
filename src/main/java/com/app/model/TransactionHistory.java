package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TransactionHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5985351114713488434L;
	private String kode_transaksi;
	private String transaction_type;
	private String reg_spaj;
	private String file_path;
	private Date tgl_transaksi;

	private String status;
	private Integer lt_id;
	private String transaction_desc;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTgl_transaksi() {
		return tgl_transaksi;
	}
	public void setTgl_transaksi(Date tgl_transaksi) {
		this.tgl_transaksi = tgl_transaksi;
	}
	public String getKode_transaksi() {
		return kode_transaksi;
	}
	public void setKode_transaksi(String kode_transaksi) {
		this.kode_transaksi = kode_transaksi;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public Integer getLt_id() {
		return lt_id;
	}

	public void setLt_id(Integer lt_id) {
		this.lt_id = lt_id;
	}

	public String getTransaction_desc() {
		return transaction_desc;
	}

	public void setTransaction_desc(String transaction_desc) {
		this.transaction_desc = transaction_desc;
	}
}