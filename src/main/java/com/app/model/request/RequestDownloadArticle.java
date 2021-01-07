package com.app.model.request;

import java.io.Serializable;

public class RequestDownloadArticle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5698967643258685817L;
	public String file_path;
	public String title;
	public String file_type;

	public String getFile_path() {
		return file_path;
	}

	public String getTitle() {
		return title;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
}