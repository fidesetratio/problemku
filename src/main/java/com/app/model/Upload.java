package com.app.model;

import java.io.Serializable;

public class Upload implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7689007551827403805L;

	private String filename;
	private String extension;
	private String attachment;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
}
