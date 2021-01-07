package com.app.model.request;

import java.io.Serializable;

public class RequestUpdateInboxStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6578270817344775014L;
	private String username;
	private String key;
	private String new_status;
	private Integer inbox_id;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNew_status() {
		return new_status;
	}

	public Integer getInbox_id() {
		return inbox_id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setNew_status(String new_status) {
		this.new_status = new_status;
	}

	public void setInbox_id(Integer inbox_id) {
		this.inbox_id = inbox_id;
	}
}
