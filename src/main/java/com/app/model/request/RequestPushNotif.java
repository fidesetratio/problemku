package com.app.model.request;

import java.io.Serializable;

public class RequestPushNotif implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4083123624084050313L;
	public Integer type;
	public String jenis_id;
	public String userid;
	public String title;
	public String message;
	public Integer next_action_menu_id;
	public String policy_number;
	public Integer priority;
	public String reg_spaj;
	public String flag_inbox;
	
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
	public Integer getNext_action_menu_id() {
		return next_action_menu_id;
	}
	public void setNext_action_menu_id(Integer next_action_menu_id) {
		this.next_action_menu_id = next_action_menu_id;
	}
	public String getPolicy_number() {
		return policy_number;
	}
	public void setPolicy_number(String policy_number) {
		this.policy_number = policy_number;
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