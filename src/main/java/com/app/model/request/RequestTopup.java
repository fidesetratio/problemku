package com.app.model.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class RequestTopup implements Serializable {

	/**
	 * @param language_id: 1 --> Indonesia, 2 --> English
	 */
	private static final long serialVersionUID = 4545652602347992927L;
	private String key;
	private String username;
	private String no_polis;
	private BigInteger mpt_id;
	private Integer lt_id;
	private String lku_id;
	private BigDecimal mpt_jumlah;
	private Integer mpt_unit;
	private Integer lus_id;
	private String payor_name;
	private String payor_occupation;
	private String payor_income;
	private String payor_source_income;
	private List funds;
	private String bsb;
	private Integer unique_code;
	private Integer transfer_type;
	private Integer language_id = 1;

	public String getKey() {
		return key;
	}

	public String getUsername() {
		return username;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public BigInteger getMpt_id() {
		return mpt_id;
	}

	public Integer getLt_id() {
		return lt_id;
	}

	public String getLku_id() {
		return lku_id;
	}

	public BigDecimal getMpt_jumlah() {
		return mpt_jumlah;
	}

	public Integer getMpt_unit() {
		return mpt_unit;
	}

	public Integer getLus_id() {
		return lus_id;
	}

	public String getPayor_name() {
		return payor_name;
	}

	public String getPayor_occupation() {
		return payor_occupation;
	}

	public String getPayor_income() {
		return payor_income;
	}

	public String getPayor_source_income() {
		return payor_source_income;
	}

	public List getFunds() {
		return funds;
	}

	public String getBsb() {
		return bsb;
	}

	public Integer getUnique_code() {
		return unique_code;
	}

	public Integer getTransfer_type() {
		return transfer_type;
	}

	public Integer getLanguage_id() {
		return language_id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public void setMpt_id(BigInteger mpt_id) {
		this.mpt_id = mpt_id;
	}

	public void setLt_id(Integer lt_id) {
		this.lt_id = lt_id;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public void setMpt_jumlah(BigDecimal mpt_jumlah) {
		this.mpt_jumlah = mpt_jumlah;
	}

	public void setMpt_unit(Integer mpt_unit) {
		this.mpt_unit = mpt_unit;
	}

	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}

	public void setPayor_name(String payor_name) {
		this.payor_name = payor_name;
	}

	public void setPayor_occupation(String payor_occupation) {
		this.payor_occupation = payor_occupation;
	}

	public void setPayor_income(String payor_income) {
		this.payor_income = payor_income;
	}

	public void setPayor_source_income(String payor_source_income) {
		this.payor_source_income = payor_source_income;
	}

	public void setFunds(List funds) {
		this.funds = funds;
	}

	public void setBsb(String bsb) {
		this.bsb = bsb;
	}

	public void setUnique_code(Integer unique_code) {
		this.unique_code = unique_code;
	}

	public void setTransfer_type(Integer transfer_type) {
		this.transfer_type = transfer_type;
	}

	public void setLanguage_id(Integer language_id) {
		this.language_id = language_id;
	}

}