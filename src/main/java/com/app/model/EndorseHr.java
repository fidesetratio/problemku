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
	private String create_date;
	private String status;
	
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}