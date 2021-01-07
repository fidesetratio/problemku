package com.app.model.request;

import java.io.Serializable;

public class RequestDownloadFileClaimSubmission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1888681457547105147L;
	private Integer type;
	private String username;
	private String key;
	private String pathFile;

	public Integer getType() {
		return type;
	}

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getPathFile() {
		return pathFile;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}
}