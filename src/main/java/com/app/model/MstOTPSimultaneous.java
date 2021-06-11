package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class MstOTPSimultaneous implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9168489352570805229L;
	private Integer id_otp;
	private Integer jenis_id;
	private String username;
	private Integer otp_no;
	private Integer attempt;
	private Date date_created;
	private Date date_expired;
	private Date date_used;
	private Integer flag_active;
	private String status;
	private Integer flag_resend;
	private String data_token;
	private Date date_blacklist;
	private Integer max_attempt;
	private Integer menu_id;

	private Integer plusDateExpired;
	private Integer plusDateBlacklist;
	private String date_created_java;

	public Integer getId_otp() {
		return id_otp;
	}

	public Integer getJenis_id() {
		return jenis_id;
	}

	public String getUsername() {
		return username;
	}

	public Integer getOtp_no() {
		return otp_no;
	}

	public Integer getAttempt() {
		return attempt;
	}

	public Date getDate_created() {
		return date_created;
	}

	public Date getDate_expired() {
		return date_expired;
	}

	public Date getDate_used() {
		return date_used;
	}

	public Integer getFlag_active() {
		return flag_active;
	}

	public String getStatus() {
		return status;
	}

	public Integer getFlag_resend() {
		return flag_resend;
	}

	public String getData_token() {
		return data_token;
	}

	public Date getDate_blacklist() {
		return date_blacklist;
	}

	public Integer getMax_attempt() {
		return max_attempt;
	}

	public Integer getMenu_id() {
		return menu_id;
	}

	public Integer getPlusDateExpired() {
		return plusDateExpired;
	}

	public Integer getPlusDateBlacklist() {
		return plusDateBlacklist;
	}

	public String getDate_created_java() {
		return date_created_java;
	}

	public void setId_otp(Integer id_otp) {
		this.id_otp = id_otp;
	}

	public void setJenis_id(Integer jenis_id) {
		this.jenis_id = jenis_id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setOtp_no(Integer otp_no) {
		this.otp_no = otp_no;
	}

	public void setAttempt(Integer attempt) {
		this.attempt = attempt;
	}

	public void setDate_created(Date date_created) {
		this.date_created = date_created;
	}

	public void setDate_expired(Date date_expired) {
		this.date_expired = date_expired;
	}

	public void setDate_used(Date date_used) {
		this.date_used = date_used;
	}

	public void setFlag_active(Integer flag_active) {
		this.flag_active = flag_active;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setFlag_resend(Integer flag_resend) {
		this.flag_resend = flag_resend;
	}

	public void setData_token(String data_token) {
		this.data_token = data_token;
	}

	public void setDate_blacklist(Date date_blacklist) {
		this.date_blacklist = date_blacklist;
	}

	public void setMax_attempt(Integer max_attempt) {
		this.max_attempt = max_attempt;
	}

	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}

	public void setPlusDateExpired(Integer plusDateExpired) {
		this.plusDateExpired = plusDateExpired;
	}

	public void setPlusDateBlacklist(Integer plusDateBlacklist) {
		this.plusDateBlacklist = plusDateBlacklist;
	}

	public void setDate_created_java(String date_created_java) {
		this.date_created_java = date_created_java;
	}
}
