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
	private String no_polis;
	private String nama_perusahaan;
	private Integer type_helpdesk;
	private String description;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNo_polis() {
		return no_polis;
	}
	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}
	public String getNama_perusahaan() {
		return nama_perusahaan;
	}
	public void setNama_perusahaan(String nama_perusahaan) {
		this.nama_perusahaan = nama_perusahaan;
	}
	public Integer getType_helpdesk() {
		return type_helpdesk;
	}
	public void setType_helpdesk(Integer type_helpdesk) {
		this.type_helpdesk = type_helpdesk;
	}
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