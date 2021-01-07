package com.app.model;

import java.io.Serializable;

public class DetailSourceRedirection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2372895219135450514L;
	private String lji_id;
	private Float mpt_persen;
	private String mpt_dk;

	public String getLji_id() {
		return lji_id;
	}

	public Float getMpt_persen() {
		return mpt_persen;
	}

	public String getMpt_dk() {
		return mpt_dk;
	}

	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}

	public void setMpt_persen(Float mpt_persen) {
		this.mpt_persen = mpt_persen;
	}

	public void setMpt_dk(String mpt_dk) {
		this.mpt_dk = mpt_dk;
	}
}