package com.app.model.request;

import java.io.Serializable;

public class RequestSubmitEndorseHr implements Serializable {

	/**
	 * @param language_id: 1 --> Indonesia, 2 --> English
	 */
	private static final long serialVersionUID = -464387748706511251L;
	private String username;
	private String key;
	private String no_polis;
	private Integer jenis_helpdesk;
	private String subject;
	private String description;
	private String attachment;
	
	public Integer getJenis_helpdesk() {
		return jenis_helpdesk;
	}
	public void setJenis_helpdesk(Integer jenis_helpdesk) {
		this.jenis_helpdesk = jenis_helpdesk;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getNo_polis() {
		return no_polis;
	}
	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
}