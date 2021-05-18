package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserHR implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8187804591408955839L;
	private String reg_spaj;
	private String no_polis;
	private String mcl_id_employee;
	private String no_hp;
	private String mste_insured;
	private String mcl_first;
	private BigDecimal lsre_id;
	private String lsre_relation;
	private String lms_status;
	private Date mspo_beg_date;
	private Date mspo_end_date;
	private BigDecimal mste_active;
	private BigDecimal mspo_type_rek;
	private String kode_reg;

	private String username;
	private String last_login_device;
	private Date update_date;
	private String eb_hr_username;
	
	public String getKode_reg() {
		return kode_reg;
	}

	public void setKode_reg(String kode_reg) {
		this.kode_reg = kode_reg;
	}

	public String getEb_hr_username() {
		return eb_hr_username;
	}

	public void setEb_hr_username(String eb_hr_username) {
		this.eb_hr_username = eb_hr_username;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public String getMcl_id_employee() {
		return mcl_id_employee;
	}

	public String getNo_hp() {
		return no_hp;
	}

	public String getMste_insured() {
		return mste_insured;
	}

	public String getMcl_first() {
		return mcl_first;
	}

	public BigDecimal getLsre_id() {
		return lsre_id;
	}

	public String getLsre_relation() {
		return lsre_relation;
	}

	public String getLms_status() {
		return lms_status;
	}

	public Date getMspo_beg_date() {
		return mspo_beg_date;
	}

	public Date getMspo_end_date() {
		return mspo_end_date;
	}

	public BigDecimal getMste_active() {
		return mste_active;
	}

	public BigDecimal getMspo_type_rek() {
		return mspo_type_rek;
	}

	public String getUsername() {
		return username;
	}

	public String getLast_login_device() {
		return last_login_device;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public void setMcl_id_employee(String mcl_id_employee) {
		this.mcl_id_employee = mcl_id_employee;
	}

	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}

	public void setMste_insured(String mste_insured) {
		this.mste_insured = mste_insured;
	}

	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}

	public void setLsre_id(BigDecimal lsre_id) {
		this.lsre_id = lsre_id;
	}

	public void setLsre_relation(String lsre_relation) {
		this.lsre_relation = lsre_relation;
	}

	public void setLms_status(String lms_status) {
		this.lms_status = lms_status;
	}

	public void setMspo_beg_date(Date mspo_beg_date) {
		this.mspo_beg_date = mspo_beg_date;
	}

	public void setMspo_end_date(Date mspo_end_date) {
		this.mspo_end_date = mspo_end_date;
	}

	public void setMste_active(BigDecimal mste_active) {
		this.mste_active = mste_active;
	}

	public void setMspo_type_rek(BigDecimal mspo_type_rek) {
		this.mspo_type_rek = mspo_type_rek;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setLast_login_device(String last_login_device) {
		this.last_login_device = last_login_device;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
}