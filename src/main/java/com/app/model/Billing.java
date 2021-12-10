package com.app.model;

import io.swagger.models.auth.In;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Billing implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 326080385768703168L;
	public static final String BILLING_PAID = "Paid";
	public static final String BILLING_OUTSTANDING = "OutS";

	public static final String BILLING_STATUS_PAID = "Paid";
	public static final String BILLING_STATUS_OUTSTANDING = "OutS";
	public static final String LABEL_STATUS_OUTSTANDING = "Outstanding";
	public static final String BILLING_STATUS_ACTIVE_BILLING = "Active";

	private String reg_spaj;
	private String lku_id;
	private String paid;
	private String lku_symbol;
	private BigDecimal total_premi;
	private BigDecimal premi_ke;
	private BigDecimal th_ke;
	private Date tgl_bayar;
	private Date periode;
	private Integer count;

	private Integer flag_jt_tempo;
	private Integer flag_topup;
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getPaid() {
		return paid;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public BigDecimal getTotal_premi() {
		return total_premi;
	}

	public BigDecimal getPremi_ke() {
		return premi_ke;
	}

	public BigDecimal getTh_ke() {
		return th_ke;
	}

	public Date getTgl_bayar() {
		return tgl_bayar;
	}

	public Date getPeriode() {
		return periode;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public void setTotal_premi(BigDecimal total_premi) {
		this.total_premi = total_premi;
	}

	public void setPremi_ke(BigDecimal premi_ke) {
		this.premi_ke = premi_ke;
	}

	public void setTh_ke(BigDecimal th_ke) {
		this.th_ke = th_ke;
	}

	public void setTgl_bayar(Date tgl_bayar) {
		this.tgl_bayar = tgl_bayar;
	}

	public void setPeriode(Date periode) {
		this.periode = periode;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public String getLku_id() {
		return lku_id;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public Integer getFlag_jt_tempo() {
		return flag_jt_tempo;
	}

	public void setFlag_jt_tempo(Integer flag_jt_tempo) {
		this.flag_jt_tempo = flag_jt_tempo;
	}

	public Integer getFlag_topup() {
		return flag_topup;
	}

	public void setFlag_topup(Integer flag_topup) {
		this.flag_topup = flag_topup;
	}
}