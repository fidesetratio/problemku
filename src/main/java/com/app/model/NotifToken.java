package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class NotifToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3444198185892209443L;
	private Integer jenis_id;
    private String userid;
    private String token;
    private Date update_date;
    private Date create_date;
    private Integer flag_active;
    
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Integer getFlag_active() {
		return flag_active;
	}
	public void setFlag_active(Integer flag_active) {
		this.flag_active = flag_active;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
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