package com.app.model.request;

import java.io.Serializable;

public class RequestValidatePolicy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3642394936265722963L;
	private String no_polis;

	public String getNo_polis() {
		return no_polis;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}
}