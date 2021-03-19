package com.app.model;

import java.io.Serializable;

public class ClaimLimit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2356621085798776512L;
	private String mspo_policy_no;
	private String lsdbs_name;
	private Integer lgc_group_id;
	private String lgc_description;
	private Integer ljj_jenis_id;
	private String ljj_jenis_jaminan;
	private String lmc_max_claim;
	private String lmc_batasan;
	private String lmc_max_batasan;
	private String mpl_max_disability;
	private String mpl_max_yearly;
	
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public Integer getLgc_group_id() {
		return lgc_group_id;
	}
	public void setLgc_group_id(Integer lgc_group_id) {
		this.lgc_group_id = lgc_group_id;
	}
	public String getLgc_description() {
		return lgc_description;
	}
	public void setLgc_description(String lgc_description) {
		this.lgc_description = lgc_description;
	}
	public Integer getLjj_jenis_id() {
		return ljj_jenis_id;
	}
	public void setLjj_jenis_id(Integer ljj_jenis_id) {
		this.ljj_jenis_id = ljj_jenis_id;
	}
	public String getLjj_jenis_jaminan() {
		return ljj_jenis_jaminan;
	}
	public void setLjj_jenis_jaminan(String ljj_jenis_jaminan) {
		this.ljj_jenis_jaminan = ljj_jenis_jaminan;
	}
	public String getLmc_max_claim() {
		return lmc_max_claim;
	}
	public void setLmc_max_claim(String lmc_max_claim) {
		this.lmc_max_claim = lmc_max_claim;
	}
	public String getLmc_batasan() {
		return lmc_batasan;
	}
	public void setLmc_batasan(String lmc_batasan) {
		this.lmc_batasan = lmc_batasan;
	}
	public String getLmc_max_batasan() {
		return lmc_max_batasan;
	}
	public void setLmc_max_batasan(String lmc_max_batasan) {
		this.lmc_max_batasan = lmc_max_batasan;
	}
	public String getMpl_max_disability() {
		return mpl_max_disability;
	}
	public void setMpl_max_disability(String mpl_max_disability) {
		this.mpl_max_disability = mpl_max_disability;
	}
	public String getMpl_max_yearly() {
		return mpl_max_yearly;
	}
	public void setMpl_max_yearly(String mpl_max_yearly) {
		this.mpl_max_yearly = mpl_max_yearly;
	}

	
}