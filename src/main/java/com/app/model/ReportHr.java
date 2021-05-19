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
	private String mpcc_id;
	private Date created_date;
	private Date date_update_status;
	private String amount_dibayar;
	private String jenis_claim;
	private String status;
	private String mspo_policy_no_format;
	private String mcl_first;
	private Date start_date;
	private Date end_date;
	private Integer double_cover_claim;
	private String bank;
	private String no_rekening;
	private String path_storage;
	private String lms_status;
	private String reason_decline;
	
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getMpcc_id() {
		return mpcc_id;
	}
	public void setMpcc_id(String mpcc_id) {
		this.mpcc_id = mpcc_id;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public Date getDate_update_status() {
		return date_update_status;
	}
	public void setDate_update_status(Date date_update_status) {
		this.date_update_status = date_update_status;
	}
	public String getAmount_dibayar() {
		return amount_dibayar;
	}
	public void setAmount_dibayar(String amount_dibayar) {
		this.amount_dibayar = amount_dibayar;
	}
	public String getJenis_claim() {
		return jenis_claim;
	}
	public void setJenis_claim(String jenis_claim) {
		this.jenis_claim = jenis_claim;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}
	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public Integer getDouble_cover_claim() {
		return double_cover_claim;
	}
	public void setDouble_cover_claim(Integer double_cover_claim) {
		this.double_cover_claim = double_cover_claim;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getNo_rekening() {
		return no_rekening;
	}
	public void setNo_rekening(String no_rekening) {
		this.no_rekening = no_rekening;
	}
	public String getPath_storage() {
		return path_storage;
	}
	public void setPath_storage(String path_storage) {
		this.path_storage = path_storage;
	}
	public String getLms_status() {
		return lms_status;
	}
	public void setLms_status(String lms_status) {
		this.lms_status = lms_status;
	}
	public String getReason_decline() {
		return reason_decline;
	}
	public void setReason_decline(String reason_decline) {
		this.reason_decline = reason_decline;
	}
}