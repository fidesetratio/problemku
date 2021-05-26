package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class EndorseHr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5577393152312579813L;
	private String id_ticket;
	private String subject;
	private String created_date;
	private String status;
	
	public String getId_ticket() {
		return id_ticket;
	}
	public void setId_ticket(String id_ticket) {
		this.id_ticket = id_ticket;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}