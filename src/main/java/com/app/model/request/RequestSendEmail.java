package com.app.model.request;

import java.io.Serializable;

public class RequestSendEmail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7353390584171431387L;
	private String from;
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String body_message;
	private String system_name;
	private Boolean isHtml;
	private String filepath;
	private Boolean showFooter;
	private Integer priority;

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getCc() {
		return cc;
	}

	public String getBcc() {
		return bcc;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody_message() {
		return body_message;
	}

	public String getSystem_name() {
		return system_name;
	}

	public Boolean getIsHtml() {
		return isHtml;
	}

	public String getFilepath() {
		return filepath;
	}

	public Boolean getShowFooter() {
		return showFooter;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setBody_message(String body_message) {
		this.body_message = body_message;
	}

	public void setSystem_name(String system_name) {
		this.system_name = system_name;
	}

	public void setIsHtml(Boolean isHtml) {
		this.isHtml = isHtml;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public void setShowFooter(Boolean showFooter) {
		this.showFooter = showFooter;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
}