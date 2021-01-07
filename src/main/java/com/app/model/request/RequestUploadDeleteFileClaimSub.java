package com.app.model.request;

import java.io.Serializable;
import java.math.BigInteger;

public class RequestUploadDeleteFileClaimSub implements Serializable {

	/**
	 * @param: 1: Upload 2: Delete one file 3: Delete Folder
	 */
	private static final long serialVersionUID = 5130473215235025445L;
	private String username;
	private String key;
	private String reg_spaj;
	private String kode_cabang;
	private Integer type;
	private BigInteger mpc_id;
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

	public String getKode_cabang() {
		return kode_cabang;
	}

	public Integer getType() {
		return type;
	}

	public BigInteger getMpc_id() {
		return mpc_id;
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

	public void setKode_cabang(String kode_cabang) {
		this.kode_cabang = kode_cabang;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setMpc_id(BigInteger mpc_id) {
		this.mpc_id = mpc_id;
	}

	public void setName_file(String name_file) {
		this.name_file = name_file;
	}

	public void setFile_base64(String file_base64) {
		this.file_base64 = file_base64;
	}
}