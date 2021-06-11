package com.app.model;

import java.io.Serializable;

public class MstOTPSimultaneousDet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2171407589944851081L;
	private Integer jenis_id;
	private Integer menu_id;
	private Integer max_attempt;
	private Integer max_attempt_resend;
	private Integer plus_date_expired;
	private Integer plus_date_blacklist;
	private Integer length_number_otp;
	private String sms_text;
	private Integer flag_active;
	private Integer max_attempt_not_resend;

	public Integer getJenis_id() {
		return jenis_id;
	}

	public Integer getMenu_id() {
		return menu_id;
	}

	public Integer getMax_attempt() {
		return max_attempt;
	}

	public Integer getMax_attempt_resend() {
		return max_attempt_resend;
	}

	public Integer getPlus_date_expired() {
		return plus_date_expired;
	}

	public Integer getPlus_date_blacklist() {
		return plus_date_blacklist;
	}

	public Integer getLength_number_otp() {
		return length_number_otp;
	}

	public String getSms_text() {
		return sms_text;
	}

	public Integer getFlag_active() {
		return flag_active;
	}

	public void setJenis_id(Integer jenis_id) {
		this.jenis_id = jenis_id;
	}

	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}

	public void setMax_attempt(Integer max_attempt) {
		this.max_attempt = max_attempt;
	}

	public void setMax_attempt_resend(Integer max_attempt_resend) {
		this.max_attempt_resend = max_attempt_resend;
	}

	public void setPlus_date_expired(Integer plus_date_expired) {
		this.plus_date_expired = plus_date_expired;
	}

	public void setPlus_date_blacklist(Integer plus_date_blacklist) {
		this.plus_date_blacklist = plus_date_blacklist;
	}

	public void setLength_number_otp(Integer length_number_otp) {
		this.length_number_otp = length_number_otp;
	}

	public void setSms_text(String sms_text) {
		this.sms_text = sms_text;
	}

	public void setFlag_active(Integer flag_active) {
		this.flag_active = flag_active;
	}

	public Integer getMax_attempt_not_resend() {
		return max_attempt_not_resend;
	}

	public void setMax_attempt_not_resend(Integer max_attempt_not_resend) {
		this.max_attempt_not_resend = max_attempt_not_resend;
	}
}
