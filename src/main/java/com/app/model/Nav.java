package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Nav implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3273280683840748199L;
	private String lji_id;
	private String lji_invest;
	private String jenis_invest;
	private String tgl;
	private String tgl_nab_0;
	private String tgl_nab_1;
	private BigDecimal nab_0;
	private BigDecimal nab_1;
	private BigDecimal nab_2;
	private BigDecimal nab_3;
	private BigDecimal nab_4;
	private BigDecimal selisih_nilai;
	private BigDecimal persen_selisih;
	private BigDecimal lnu_nilai;
	private BigDecimal selisih;
	private BigDecimal persen_hmin1;
	private BigDecimal persen_hke1;
	private BigDecimal nilai_hke1;

	public String getLji_id() {
		return lji_id;
	}

	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}

	public String getLji_invest() {
		return lji_invest;
	}

	public void setLji_invest(String lji_invest) {
		this.lji_invest = lji_invest;
	}

	public String getJenis_invest() {
		return jenis_invest;
	}

	public void setJenis_invest(String jenis_invest) {
		this.jenis_invest = jenis_invest;
	}

	public String getTgl() {
		return tgl;
	}

	public void setTgl(String tgl) {
		this.tgl = tgl;
	}

	public String getTgl_nab_0() {
		return tgl_nab_0;
	}

	public void setTgl_nab_0(String tgl_nab_0) {
		this.tgl_nab_0 = tgl_nab_0;
	}

	public String getTgl_nab_1() {
		return tgl_nab_1;
	}

	public void setTgl_nab_1(String tgl_nab_1) {
		this.tgl_nab_1 = tgl_nab_1;
	}

	public BigDecimal getNab_0() {
		return nab_0;
	}

	public void setNab_0(BigDecimal nab_0) {
		this.nab_0 = nab_0;
	}

	public BigDecimal getNab_1() {
		return nab_1;
	}

	public void setNab_1(BigDecimal nab_1) {
		this.nab_1 = nab_1;
	}

	public BigDecimal getNab_2() {
		return nab_2;
	}

	public void setNab_2(BigDecimal nab_2) {
		this.nab_2 = nab_2;
	}

	public BigDecimal getNab_3() {
		return nab_3;
	}

	public void setNab_3(BigDecimal nab_3) {
		this.nab_3 = nab_3;
	}

	public BigDecimal getNab_4() {
		return nab_4;
	}

	public void setNab_4(BigDecimal nab_4) {
		this.nab_4 = nab_4;
	}

	public BigDecimal getSelisih_nilai() {
		return selisih_nilai;
	}

	public void setSelisih_nilai(BigDecimal selisih_nilai) {
		this.selisih_nilai = selisih_nilai;
	}

	public BigDecimal getPersen_selisih() {
		return persen_selisih;
	}

	public void setPersen_selisih(BigDecimal persen_selisih) {
		this.persen_selisih = persen_selisih;
	}

	public BigDecimal getLnu_nilai() {
		return lnu_nilai;
	}

	public void setLnu_nilai(BigDecimal lnu_nilai) {
		this.lnu_nilai = lnu_nilai;
	}

	public BigDecimal getSelisih() {
		return selisih;
	}

	public void setSelisih(BigDecimal selisih) {
		this.selisih = selisih;
	}

	public BigDecimal getPersen_hmin1() {
		return persen_hmin1;
	}

	public void setPersen_hmin1(BigDecimal persen_hmin1) {
		this.persen_hmin1 = persen_hmin1;
	}

	public BigDecimal getPersen_hke1() {
		return persen_hke1;
	}

	public void setPersen_hke1(BigDecimal persen_hke1) {
		this.persen_hke1 = persen_hke1;
	}

	public BigDecimal getNilai_hke1() {
		return nilai_hke1;
	}

	public void setNilai_hke1(BigDecimal nilai_hke1) {
		this.nilai_hke1 = nilai_hke1;
	}
}