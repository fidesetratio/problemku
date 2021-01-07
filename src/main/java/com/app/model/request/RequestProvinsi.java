package com.app.model.request;

import java.io.Serializable;

public class RequestProvinsi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2418264305865926912L;
	private String username;
	private String key;
	private Integer requestCode;
	private Integer lspr_id;
	private Integer lska_id;
	private Integer lskc_id;
	private Integer lskl_id;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public Integer getRequestCode() {
		return requestCode;
	}

	public Integer getLspr_id() {
		return lspr_id;
	}

	public Integer getLska_id() {
		return lska_id;
	}

	public Integer getLskc_id() {
		return lskc_id;
	}

	public Integer getLskl_id() {
		return lskl_id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setRequestCode(Integer requestCode) {
		this.requestCode = requestCode;
	}

	public void setLspr_id(Integer lspr_id) {
		this.lspr_id = lspr_id;
	}

	public void setLska_id(Integer lska_id) {
		this.lska_id = lska_id;
	}

	public void setLskc_id(Integer lskc_id) {
		this.lskc_id = lskc_id;
	}

	public void setLskl_id(Integer lskl_id) {
		this.lskl_id = lskl_id;
	}
}
