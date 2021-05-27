package com.app.model.request;

import java.io.Serializable;

public class RequestViewEndorseHr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7593991121805604265L;
	private String username;
	private String key;
	private String id_ticket;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getId_ticket() {
		return id_ticket;
	}
	public void setId_ticket(String id_ticket) {
		this.id_ticket = id_ticket;
	}
}