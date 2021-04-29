package com.app.model.request;

import java.io.Serializable;

public class RequestSaveToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4083123624084050313L;
	public String userid;
	public Integer jenis_id;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Integer getJenis_id() {
		return jenis_id;
	}
	public void setJenis_id(Integer jenis_id) {
		this.jenis_id = jenis_id;
	}
}