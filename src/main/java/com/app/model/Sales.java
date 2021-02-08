package com.app.model;

import java.io.Serializable;

public class Sales implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7689007551827403805L;

	private String reg_spaj;
	private String mspo_policy_no;
	private String mcl_first;
	private String msag_smart_no;
	private String mspe_email;
	private String mcl_first_leader;
	private String msag_smart_no_leader;
	private String mspe_email_leader;
	
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public String getMsag_smart_no() {
		return msag_smart_no;
	}
	public void setMsag_smart_no(String msag_smart_no) {
		this.msag_smart_no = msag_smart_no;
	}
	public String getMspe_email() {
		return mspe_email;
	}
	public void setMspe_email(String mspe_email) {
		this.mspe_email = mspe_email;
	}
	public String getMcl_first_leader() {
		return mcl_first_leader;
	}
	public void setMcl_first_leader(String mcl_first_leader) {
		this.mcl_first_leader = mcl_first_leader;
	}
	public String getMsag_smart_no_leader() {
		return msag_smart_no_leader;
	}
	public void setMsag_smart_no_leader(String msag_smart_no_leader) {
		this.msag_smart_no_leader = msag_smart_no_leader;
	}
	public String getMspe_email_leader() {
		return mspe_email_leader;
	}
	public void setMspe_email_leader(String mspe_email_leader) {
		this.mspe_email_leader = mspe_email_leader;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
}
