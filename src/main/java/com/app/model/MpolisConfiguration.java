package com.app.model;

import java.util.Date;

public class MpolisConfiguration {
	private int conf_id;
	private String conf_name;
	private String conf_value;
	private int active;
	private Date update_date;
	private String description;

	public int getConf_id() {
		return conf_id;
	}

	public void setConf_id(int conf_id) {
		this.conf_id = conf_id;
	}

	public String getConf_name() {
		return conf_name;
	}

	public void setConf_name(String conf_name) {
		this.conf_name = conf_name;
	}

	public String getConf_value() {
		return conf_value;
	}

	public void setConf_value(String conf_value) {
		this.conf_value = conf_value;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}