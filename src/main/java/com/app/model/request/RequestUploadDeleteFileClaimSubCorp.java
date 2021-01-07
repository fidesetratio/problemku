package com.app.model.request;

import java.io.Serializable;

public class RequestUploadDeleteFileClaimSubCorp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1206229624399087233L;
	private String username;
	private String key;
	private String reg_spaj;
	private String mste_insured;
	private Integer type;
	private String mpcc_id;
	private String name_file;
	private String file_base64;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public String getMste_insured() {
		return mste_insured;
	}

	public Integer getType() {
		return type;
	}

	public String getMpcc_id() {
		return mpcc_id;
	}

	public String getName_file() {
		return name_file;
	}

	public String getFile_base64() {
		return file_base64;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public void setMste_insured(String mste_insured) {
		this.mste_insured = mste_insured;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setMpcc_id(String mpcc_id) {
		this.mpcc_id = mpcc_id;
	}

	public void setName_file(String name_file) {
		this.name_file = name_file;
	}

	public void setFile_base64(String file_base64) {
		this.file_base64 = file_base64;
	}
}