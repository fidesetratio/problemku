package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class Withdraw implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 135235080588693905L;
	private String mpt_id;
	private Date req_date;
	private Date date_status;
	private String description;
	private BigDecimal total_penarikan;
	private Integer lt_id;
	private String lt_transaksi;
	private String payor_name;
	private String rekening;
	private String bank_name;
	private String mspo_policy_no_format;
	private String reason_fu;
	private String lku_id;
	private String lku_symbol;
	private String status_polis;
	private String nm_product;
	private BigDecimal mpt_jumlah;
	private BigDecimal mpt_unit;
	private ArrayList<DetailWithdraw> detail_withdraw;
	private BigDecimal biaya;
	private String type_penarikan;
	private BigDecimal mpt_jumlah_detail;
	private BigDecimal mpt_unit_detail;
	private BigDecimal count_value;
	private BigDecimal premi_pokok;
	private BigDecimal premi_topup;
	private BigDecimal lsbs_id;
	private BigDecimal lsdbs_number;
	private String no_hp;
	private String email;

	public String getMpt_id() {
		return mpt_id;
	}

	public Date getReq_date() {
		return req_date;
	}

	public Date getDate_status() {
		return date_status;
	}

	public String getDescription() {
		return description;
	}

	public BigDecimal getTotal_penarikan() {
		return total_penarikan;
	}

	public Integer getLt_id() {
		return lt_id;
	}

	public String getLt_transaksi() {
		return lt_transaksi;
	}

	public String getPayor_name() {
		return payor_name;
	}

	public String getRekening() {
		return rekening;
	}

	public String getBank_name() {
		return bank_name;
	}

	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}

	public String getReason_fu() {
		return reason_fu;
	}

	public String getLku_id() {
		return lku_id;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public String getStatus_polis() {
		return status_polis;
	}

	public String getNm_product() {
		return nm_product;
	}

	public BigDecimal getMpt_jumlah() {
		return mpt_jumlah;
	}

	public BigDecimal getMpt_unit() {
		return mpt_unit;
	}

	public ArrayList<DetailWithdraw> getDetail_withdraw() {
		return detail_withdraw;
	}

	public BigDecimal getBiaya() {
		return biaya;
	}

	public String getType_penarikan() {
		return type_penarikan;
	}

	public BigDecimal getMpt_jumlah_detail() {
		return mpt_jumlah_detail;
	}

	public BigDecimal getMpt_unit_detail() {
		return mpt_unit_detail;
	}

	public BigDecimal getCount_value() {
		return count_value;
	}

	public BigDecimal getPremi_pokok() {
		return premi_pokok;
	}

	public BigDecimal getPremi_topup() {
		return premi_topup;
	}

	public BigDecimal getLsbs_id() {
		return lsbs_id;
	}

	public BigDecimal getLsdbs_number() {
		return lsdbs_number;
	}

	public String getNo_hp() {
		return no_hp;
	}

	public String getEmail() {
		return email;
	}

	public void setMpt_id(String mpt_id) {
		this.mpt_id = mpt_id;
	}

	public void setReq_date(Date req_date) {
		this.req_date = req_date;
	}

	public void setDate_status(Date date_status) {
		this.date_status = date_status;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTotal_penarikan(BigDecimal total_penarikan) {
		this.total_penarikan = total_penarikan;
	}

	public void setLt_id(Integer lt_id) {
		this.lt_id = lt_id;
	}

	public void setLt_transaksi(String lt_transaksi) {
		this.lt_transaksi = lt_transaksi;
	}

	public void setPayor_name(String payor_name) {
		this.payor_name = payor_name;
	}

	public void setRekening(String rekening) {
		this.rekening = rekening;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}

	public void setReason_fu(String reason_fu) {
		this.reason_fu = reason_fu;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public void setStatus_polis(String status_polis) {
		this.status_polis = status_polis;
	}

	public void setNm_product(String nm_product) {
		this.nm_product = nm_product;
	}

	public void setMpt_jumlah(BigDecimal mpt_jumlah) {
		this.mpt_jumlah = mpt_jumlah;
	}

	public void setMpt_unit(BigDecimal mpt_unit) {
		this.mpt_unit = mpt_unit;
	}

	public void setDetail_withdraw(ArrayList<DetailWithdraw> detail_withdraw) {
		this.detail_withdraw = detail_withdraw;
	}

	public void setBiaya(BigDecimal biaya) {
		this.biaya = biaya;
	}

	public void setType_penarikan(String type_penarikan) {
		this.type_penarikan = type_penarikan;
	}

	public void setMpt_jumlah_detail(BigDecimal mpt_jumlah_detail) {
		this.mpt_jumlah_detail = mpt_jumlah_detail;
	}

	public void setMpt_unit_detail(BigDecimal mpt_unit_detail) {
		this.mpt_unit_detail = mpt_unit_detail;
	}

	public void setCount_value(BigDecimal count_value) {
		this.count_value = count_value;
	}

	public void setPremi_pokok(BigDecimal premi_pokok) {
		this.premi_pokok = premi_pokok;
	}

	public void setPremi_topup(BigDecimal premi_topup) {
		this.premi_topup = premi_topup;
	}

	public void setLsbs_id(BigDecimal lsbs_id) {
		this.lsbs_id = lsbs_id;
	}

	public void setLsdbs_number(BigDecimal lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}

	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}