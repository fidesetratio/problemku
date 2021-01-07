package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ClaimSubmissionCorporate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7184145852242191708L;
	// Jenis Claim
	private String label;

	// Claim Submission Corporate
	private String mspo_policy_no_format;
	private String mcl_first;
	private String mste_no_reg;
	private String msbc_bank;
	private String msbc_acc_no;
	private BigDecimal mspo_type_rek;
	private String document_name;
	private String lgc_description;
	private BigDecimal lssp_id;
	private String mspe_mobile;

	// List Claim
	private Date created_date;
	private Date date_update_status;
	private String mpcc_id;
	private BigDecimal amount_dibayar;
	private String jenis_claim;
	private String status;
	private String reason_further;
	private BigDecimal flag_susulan;
	private BigDecimal id_status;

	// View Claim
	private Date start_date;
	private Date end_date;
	private Integer double_cover_claim;
	private String bank;
	private String no_rekening;
	private String path_storage;
	private String lms_status;
	private String reason_decline;

	public String getLabel() {
		return label;
	}

	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}

	public String getMcl_first() {
		return mcl_first;
	}

	public String getMste_no_reg() {
		return mste_no_reg;
	}

	public String getMsbc_bank() {
		return msbc_bank;
	}

	public String getMsbc_acc_no() {
		return msbc_acc_no;
	}

	public BigDecimal getMspo_type_rek() {
		return mspo_type_rek;
	}

	public String getDocument_name() {
		return document_name;
	}

	public String getLgc_description() {
		return lgc_description;
	}

	public BigDecimal getLssp_id() {
		return lssp_id;
	}

	public String getMspe_mobile() {
		return mspe_mobile;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public Date getDate_update_status() {
		return date_update_status;
	}

	public String getMpcc_id() {
		return mpcc_id;
	}

	public BigDecimal getAmount_dibayar() {
		return amount_dibayar;
	}

	public String getJenis_claim() {
		return jenis_claim;
	}

	public String getStatus() {
		return status;
	}

	public String getReason_further() {
		return reason_further;
	}

	public BigDecimal getFlag_susulan() {
		return flag_susulan;
	}

	public BigDecimal getId_status() {
		return id_status;
	}

	public Date getStart_date() {
		return start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public Integer getDouble_cover_claim() {
		return double_cover_claim;
	}

	public String getBank() {
		return bank;
	}

	public String getNo_rekening() {
		return no_rekening;
	}

	public String getPath_storage() {
		return path_storage;
	}

	public String getLms_status() {
		return lms_status;
	}

	public String getReason_decline() {
		return reason_decline;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}

	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}

	public void setMste_no_reg(String mste_no_reg) {
		this.mste_no_reg = mste_no_reg;
	}

	public void setMsbc_bank(String msbc_bank) {
		this.msbc_bank = msbc_bank;
	}

	public void setMsbc_acc_no(String msbc_acc_no) {
		this.msbc_acc_no = msbc_acc_no;
	}

	public void setMspo_type_rek(BigDecimal mspo_type_rek) {
		this.mspo_type_rek = mspo_type_rek;
	}

	public void setDocument_name(String document_name) {
		this.document_name = document_name;
	}

	public void setLgc_description(String lgc_description) {
		this.lgc_description = lgc_description;
	}

	public void setLssp_id(BigDecimal lssp_id) {
		this.lssp_id = lssp_id;
	}

	public void setMspe_mobile(String mspe_mobile) {
		this.mspe_mobile = mspe_mobile;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public void setDate_update_status(Date date_update_status) {
		this.date_update_status = date_update_status;
	}

	public void setMpcc_id(String mpcc_id) {
		this.mpcc_id = mpcc_id;
	}

	public void setAmount_dibayar(BigDecimal amount_dibayar) {
		this.amount_dibayar = amount_dibayar;
	}

	public void setJenis_claim(String jenis_claim) {
		this.jenis_claim = jenis_claim;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setReason_further(String reason_further) {
		this.reason_further = reason_further;
	}

	public void setFlag_susulan(BigDecimal flag_susulan) {
		this.flag_susulan = flag_susulan;
	}

	public void setId_status(BigDecimal id_status) {
		this.id_status = id_status;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public void setDouble_cover_claim(Integer double_cover_claim) {
		this.double_cover_claim = double_cover_claim;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public void setNo_rekening(String no_rekening) {
		this.no_rekening = no_rekening;
	}

	public void setPath_storage(String path_storage) {
		this.path_storage = path_storage;
	}

	public void setLms_status(String lms_status) {
		this.lms_status = lms_status;
	}

	public void setReason_decline(String reason_decline) {
		this.reason_decline = reason_decline;
	}
}