package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class BenefitCorporate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5610616490825189309L;
	private String nm_plan;
	private String fasilitas;
	private String reimburse;
	private BigDecimal naik_kelas;
	private BigDecimal batas_benefit_tahunan;
	private BigDecimal limit_benefit;
	private BigDecimal jml_pemakaian;
	private BigDecimal sisa_limit;
	
	public BigDecimal getLimit_benefit() {
		return limit_benefit;
	}

	public void setLimit_benefit(BigDecimal limit_benefit) {
		this.limit_benefit = limit_benefit;
	}

	public BigDecimal getJml_pemakaian() {
		return jml_pemakaian;
	}

	public void setJml_pemakaian(BigDecimal jml_pemakaian) {
		this.jml_pemakaian = jml_pemakaian;
	}

	public BigDecimal getSisa_limit() {
		return sisa_limit;
	}

	public void setSisa_limit(BigDecimal sisa_limit) {
		this.sisa_limit = sisa_limit;
	}

	public String getNm_plan() {
		return nm_plan;
	}

	public String getFasilitas() {
		return fasilitas;
	}

	public String getReimburse() {
		return reimburse;
	}

	public BigDecimal getNaik_kelas() {
		return naik_kelas;
	}

	public BigDecimal getBatas_benefit_tahunan() {
		return batas_benefit_tahunan;
	}

	public void setNm_plan(String nm_plan) {
		this.nm_plan = nm_plan;
	}

	public void setFasilitas(String fasilitas) {
		this.fasilitas = fasilitas;
	}

	public void setReimburse(String reimburse) {
		this.reimburse = reimburse;
	}

	public void setNaik_kelas(BigDecimal naik_kelas) {
		this.naik_kelas = naik_kelas;
	}

	public void setBatas_benefit_tahunan(BigDecimal batas_benefit_tahunan) {
		this.batas_benefit_tahunan = batas_benefit_tahunan;
	}
}
