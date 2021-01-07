package com.app.model.request;

import java.io.Serializable;

public class RequestDocumentClaimSubmissionCorporate implements Serializable {

	/**
	 * @param language_id --> 1: Indonesia, 2: English
	 */
	private static final long serialVersionUID = -2649183776765778764L;
	private String username;
	private String key;
	private String jenis_claim;
	private Integer language_id;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getJenis_claim() {
		return jenis_claim;
	}

	public Integer getLanguage_id() {
		return language_id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setJenis_claim(String jenis_claim) {
		this.jenis_claim = jenis_claim;
	}

	public void setLanguage_id(Integer language_id) {
		this.language_id = language_id;
	}
}