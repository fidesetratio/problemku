package com.app.model.request;

import java.io.Serializable;

import com.app.model.Withdraw;

public class RequestSubmitWithdraw implements Serializable {

	/**
	 * @param language_id: 1 --> Indonesia, 2 --> English
	 */
	private static final long serialVersionUID = -7489814202979112832L;
	private String username;
	private String key;
	private String no_polis;
	private Integer language_id = 1;
	private Withdraw withdraw;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public Integer getLanguage_id() {
		return language_id;
	}

	public Withdraw getWithdraw() {
		return withdraw;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public void setLanguage_id(Integer language_id) {
		this.language_id = language_id;
	}

	public void setWithdraw(Withdraw withdraw) {
		this.withdraw = withdraw;
	}
}