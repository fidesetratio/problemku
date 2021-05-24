package com.app.model.request;

import java.io.Serializable;

public class RequestViewMclFirst implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5679664754322258278L;
	private String msag_id;
	private String mspo_policy_no;
	private String mcl_first;
	
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}

	
}