package com.app.model.request;

import java.io.Serializable;

public class RequestInbox implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4083123624084050313L;
	public String userid;
	public Integer jenis_id;
	public String token;
	
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}