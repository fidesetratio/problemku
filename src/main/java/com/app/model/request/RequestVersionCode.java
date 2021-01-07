package com.app.model.request;

import java.io.Serializable;

public class RequestVersionCode implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 6116996427004495441L;
	public Integer flag_app;
	public Integer flag_platform;

	public Integer getFlag_app() {
		return flag_app;
	}

	public Integer getFlag_platform() {
		return flag_platform;
	}

	public void setFlag_app(Integer flag_app) {
		this.flag_app = flag_app;
	}

	public void setFlag_platform(Integer flag_platform) {
		this.flag_platform = flag_platform;
	}
}