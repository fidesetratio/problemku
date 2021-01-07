package com.app.model.request;

import java.io.Serializable;

public class RequestCheckStatusTransaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4379364905803344533L;
	private String username;
	private String key;
	private String no_polis;
	private Integer menu_id_transaction;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public Integer getMenu_id_transaction() {
		return menu_id_transaction;
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

	public void setMenu_id_transaction(Integer menu_id_transaction) {
		this.menu_id_transaction = menu_id_transaction;
	}
}