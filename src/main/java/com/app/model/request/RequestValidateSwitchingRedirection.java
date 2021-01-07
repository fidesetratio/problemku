package com.app.model.request;

import com.app.model.Redirection;
import com.app.model.Switching;

public class RequestValidateSwitchingRedirection {

	private String username;
	private String key;
	private String no_polis;
	private Integer lt_id;
	private Switching switching;
	private Redirection redirection;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public Integer getLt_id() {
		return lt_id;
	}

	public Switching getSwitching() {
		return switching;
	}

	public Redirection getRedirection() {
		return redirection;
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

	public void setLt_id(Integer lt_id) {
		this.lt_id = lt_id;
	}

	public void setSwitching(Switching switching) {
		this.switching = switching;
	}

	public void setRedirection(Redirection redirection) {
		this.redirection = redirection;
	}
}
