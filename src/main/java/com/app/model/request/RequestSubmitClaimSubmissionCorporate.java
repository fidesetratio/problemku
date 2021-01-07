package com.app.model.request;

import java.io.Serializable;

public class RequestSubmitClaimSubmissionCorporate implements Serializable {

	/**
	 * @param language_id: 1 --> Indonesia, 2 --> English
	 */
	private static final long serialVersionUID = -464387748706511251L;
	private String username;
	private String key;
	private String mpcc_id;
	private String reg_spaj;
	private String mste_insured;
	private String no_reg;
	private Integer lssp_id;
	private String jenis_claim;
	private String start_date;
	private String end_date;
	private Boolean double_cover_claim;
	private String bank;
	private String no_rekening;
	private Integer language_id = 1;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getMpcc_id() {
		return mpcc_id;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public String getMste_insured() {
		return mste_insured;
	}

	public String getNo_reg() {
		return no_reg;
	}

	public Integer getLssp_id() {
		return lssp_id;
	}

	public String getJenis_claim() {
		return jenis_claim;
	}

	public String getStart_date() {
		return start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public Boolean getDouble_cover_claim() {
		return double_cover_claim;
	}

	public String getBank() {
		return bank;
	}

	public String getNo_rekening() {
		return no_rekening;
	}

	public Integer getLanguage_id() {
		return language_id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setMpcc_id(String mpcc_id) {
		this.mpcc_id = mpcc_id;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public void setMste_insured(String mste_insured) {
		this.mste_insured = mste_insured;
	}

	public void setNo_reg(String no_reg) {
		this.no_reg = no_reg;
	}

	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}

	public void setJenis_claim(String jenis_claim) {
		this.jenis_claim = jenis_claim;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public void setDouble_cover_claim(Boolean double_cover_claim) {
		this.double_cover_claim = double_cover_claim;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public void setNo_rekening(String no_rekening) {
		this.no_rekening = no_rekening;
	}

	public void setLanguage_id(Integer language_id) {
		this.language_id = language_id;
	}
}