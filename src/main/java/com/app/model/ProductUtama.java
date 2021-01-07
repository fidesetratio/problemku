package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductUtama implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1295417493365632610L;
	private BigDecimal lsbs_id;
	private BigDecimal lsdbs_number;

	public BigDecimal getLsbs_id() {
		return lsbs_id;
	}

	public void setLsbs_id(BigDecimal lsbs_id) {
		this.lsbs_id = lsbs_id;
	}

	public BigDecimal getLsdbs_number() {
		return lsdbs_number;
	}

	public void setLsdbs_number(BigDecimal lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
}