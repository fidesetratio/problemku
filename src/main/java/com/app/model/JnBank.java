package com.app.model;

public class JnBank {

	private Integer jn_bank;
	private String nm_bank;
	public JnBank(Integer jn_bank, String nm_bank) {
		super();
		this.jn_bank = jn_bank;
		this.nm_bank = nm_bank;
	}
	public JnBank() {
		super();
		
	}
	public Integer getJn_bank() {
		return jn_bank;
	}
	public void setJn_bank(Integer jn_bank) {
		this.jn_bank = jn_bank;
	}
	public String getNm_bank() {
		return nm_bank;
	}
	public void setNm_bank(String nm_bank) {
		this.nm_bank = nm_bank;
	}


}
