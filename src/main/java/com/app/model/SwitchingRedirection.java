package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class SwitchingRedirection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8677591993068062956L;
	private String mspo_policy_no_format;
	private String payor_name;
	private String status_polis;
	private String nm_product;
	private Date req_date;
	private String lt_transksi;
	private BigDecimal mpt_jumlah;
	private BigDecimal mpt_jumlah_process;
	private BigDecimal mpt_unit;
	private BigDecimal lt_id;
	private String reason_fu;
	private String description;
	private Date date_status;
	private String mpt_id;
	private String lji_id;
	private Float mpt_persen;
	private String mpt_dk;
	private String jenis_transaksi;
	private String lku_symbol;
	private String lji_invest;
	private String id;

	private String date_created_java1;
	private String date_created_java2;
	private String reg_spaj;
	private String lku_id;
	private BigInteger mpt_id_switching;
	private BigDecimal biaya;
	private String type_penarikan;

	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}

	public String getPayor_name() {
		return payor_name;
	}

	public String getStatus_polis() {
		return status_polis;
	}

	public String getNm_product() {
		return nm_product;
	}

	public Date getReq_date() {
		return req_date;
	}

	public String getLt_transksi() {
		return lt_transksi;
	}

	public BigDecimal getMpt_jumlah() {
		return mpt_jumlah;
	}

	public BigDecimal getMpt_jumlah_process() {
		return mpt_jumlah_process;
	}

	public BigDecimal getMpt_unit() {
		return mpt_unit;
	}

	public BigDecimal getLt_id() {
		return lt_id;
	}

	public String getReason_fu() {
		return reason_fu;
	}

	public String getDescription() {
		return description;
	}

	public Date getDate_status() {
		return date_status;
	}

	public String getMpt_id() {
		return mpt_id;
	}

	public String getLji_id() {
		return lji_id;
	}

	public Float getMpt_persen() {
		return mpt_persen;
	}

	public String getMpt_dk() {
		return mpt_dk;
	}

	public String getJenis_transaksi() {
		return jenis_transaksi;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public String getLji_invest() {
		return lji_invest;
	}

	public String getId() {
		return id;
	}

	public String getDate_created_java1() {
		return date_created_java1;
	}

	public String getDate_created_java2() {
		return date_created_java2;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public String getLku_id() {
		return lku_id;
	}

	public BigInteger getMpt_id_switching() {
		return mpt_id_switching;
	}

	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}

	public void setPayor_name(String payor_name) {
		this.payor_name = payor_name;
	}

	public void setStatus_polis(String status_polis) {
		this.status_polis = status_polis;
	}

	public void setNm_product(String nm_product) {
		this.nm_product = nm_product;
	}

	public void setReq_date(Date req_date) {
		this.req_date = req_date;
	}

	public void setLt_transksi(String lt_transksi) {
		this.lt_transksi = lt_transksi;
	}

	public void setMpt_jumlah(BigDecimal mpt_jumlah) {
		this.mpt_jumlah = mpt_jumlah;
	}

	public void setMpt_jumlah_process(BigDecimal mpt_jumlah_process) {
		this.mpt_jumlah_process = mpt_jumlah_process;
	}

	public void setMpt_unit(BigDecimal mpt_unit) {
		this.mpt_unit = mpt_unit;
	}

	public void setLt_id(BigDecimal lt_id) {
		this.lt_id = lt_id;
	}

	public void setReason_fu(String reason_fu) {
		this.reason_fu = reason_fu;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate_status(Date date_status) {
		this.date_status = date_status;
	}

	public void setMpt_id(String mpt_id) {
		this.mpt_id = mpt_id;
	}

	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}

	public void setMpt_persen(Float mpt_persen) {
		this.mpt_persen = mpt_persen;
	}

	public void setMpt_dk(String mpt_dk) {
		this.mpt_dk = mpt_dk;
	}

	public void setJenis_transaksi(String jenis_transaksi) {
		this.jenis_transaksi = jenis_transaksi;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public void setLji_invest(String lji_invest) {
		this.lji_invest = lji_invest;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDate_created_java1(String date_created_java1) {
		this.date_created_java1 = date_created_java1;
	}

	public void setDate_created_java2(String date_created_java2) {
		this.date_created_java2 = date_created_java2;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public void setMpt_id_switching(BigInteger mpt_id_switching) {
		this.mpt_id_switching = mpt_id_switching;
	}

	public BigDecimal getBiaya() {
		return biaya;
	}

	public void setBiaya(BigDecimal biaya) {
		this.biaya = biaya;
	}

	public String getType_penarikan() {
		return type_penarikan;
	}

	public void setType_penarikan(String type_penarikan) {
		this.type_penarikan = type_penarikan;
	}
}