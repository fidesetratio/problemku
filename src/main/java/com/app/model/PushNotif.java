package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PushNotif implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6282886395048420294L;

	private Integer id;
	private String jenis_id;
	private String username;
	private String token;
	private String title;
	private String message;
	private String parameter;
	private Integer priority;
	private Integer lus_id;
	private String reg_spaj;
	private String status;
	private String keterangan;
	private String flag_inbox;
	private Date create_date;
	private Date sent_date;
	private String host_name;
	private Date host_date;
	
	public String getJenis_id() {
		return jenis_id;
	}
	public void setJenis_id(String jenis_id) {
		this.jenis_id = jenis_id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getFlag_inbox() {
		return flag_inbox;
	}
	public void setFlag_inbox(String flag_inbox) {
		this.flag_inbox = flag_inbox;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getSent_date() {
		return sent_date;
	}
	public void setSent_date(Date sent_date) {
		this.sent_date = sent_date;
	}
	public String getHost_name() {
		return host_name;
	}
	public void setHost_name(String host_name) {
		this.host_name = host_name;
	}
	public Date getHost_date() {
		return host_date;
	}
	public void setHost_date(Date host_date) {
		this.host_date = host_date;
	}
}