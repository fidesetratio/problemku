package com.app.model.request;

import java.io.Serializable;

import org.json.JSONObject;

import com.app.model.Data;

public class RequestPushNotif implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4083123624084050313L;
	private Integer type;
	private String jenis_id;
	private String userid;
	private String title;
	private String message;
	private Integer priority;
	private String reg_spaj;
	private String flag_inbox;
	private Data data;
	
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getJenis_id() {
		return jenis_id;
	}
	public void setJenis_id(String jenis_id) {
		this.jenis_id = jenis_id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getFlag_inbox() {
		return flag_inbox;
	}
	public void setFlag_inbox(String flag_inbox) {
		this.flag_inbox = flag_inbox;
	}
	
	
}