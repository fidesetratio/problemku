package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class VersionCode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2876496491920239169L;
	private String app_name;
	private String version_name;
	private BigDecimal version_code;
	private String desc_app;

	public String getApp_name() {
		return app_name;
	}

	public String getVersion_name() {
		return version_name;
	}

	public BigDecimal getVersion_code() {
		return version_code;
	}

	public String getDesc_app() {
		return desc_app;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}

	public void setVersion_code(BigDecimal version_code) {
		this.version_code = version_code;
	}

	public void setDesc_app(String desc_app) {
		this.desc_app = desc_app;
	}
}