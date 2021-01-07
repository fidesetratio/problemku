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
