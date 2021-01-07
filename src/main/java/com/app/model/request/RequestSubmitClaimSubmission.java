package com.app.model.request;

import java.io.Serializable;
import java.math.BigInteger;

public class RequestSubmitClaimSubmission implements Serializable {

	/**
	 * @param jenisclaim: 1: Rawat Jalan 2: Rawat Inap 3: Rawat Gigi 5: Rawat
	 *        Bersalin
	 * @param language_id: 1 --> Indonesia, 2 --> English
	 */
	private static final long serialVersionUID = -5267156778027634371L;
	private String username;
	private String key;
	private String no_polis;
	private BigInteger mpc_id;
	private Integer mste_insured_no;
	private String nm_pemegang;
	private String patienname;
	private String lku_id;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private String date_ri_1;
	private String date_ri_2;
	private Integer amount_ri;
	private Integer jenisclaim;
	private String no_hp;
	private String email;
	private Integer lssp_id;
	private String groupclaimjenis;
	private String accountno;
	private Integer lsbp_id;
	private String nama_cabang;
	private String atas_nama;
	private Boolean double_cover_claim;
	private String jenis_tertanggung;
	private Integer language_id = 1;

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public BigInteger getMpc_id() {
		return mpc_id;
	}

	public Integer getMste_insured_no() {
		return mste_insured_no;
	}

	public String getNm_pemegang() {
		return nm_pemegang;
	}

	public String getPatienname() {
		return patienname;
	}

	public String getLku_id() {
		return lku_id;
	}

	public Integer getLsbs_id() {
		return lsbs_id;
	}

	public Integer getLsdbs_number() {
		return lsdbs_number;
	}

	public String getDate_ri_1() {
		return date_ri_1;
	}

	public String getDate_ri_2() {
		return date_ri_2;
	}

	public Integer getAmount_ri() {
		return amount_ri;
	}

	public Integer getJenisclaim() {
		return jenisclaim;
	}

	public String getNo_hp() {
		return no_hp;
	}

	public String getEmail() {
		return email;
	}

	public Integer getLssp_id() {
		return lssp_id;
	}

	public String getGroupclaimjenis() {
		return groupclaimjenis;
	}

	public String getAccountno() {
		return accountno;
	}

	public Integer getLsbp_id() {
		return lsbp_id;
	}

	public String getNama_cabang() {
		return nama_cabang;
	}

	public String getAtas_nama() {
		return atas_nama;
	}

	public Boolean getDouble_cover_claim() {
		return double_cover_claim;
	}

	public String getJenis_tertanggung() {
		return jenis_tertanggung;
	}

	public Integer getLanguage_id() {
		return language_id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public void setMpc_id(BigInteger mpc_id) {
		this.mpc_id = mpc_id;
	}

	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}

	public void setNm_pemegang(String nm_pemegang) {
		this.nm_pemegang = nm_pemegang;
	}

	public void setPatienname(String patienname) {
		this.patienname = patienname;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}

	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}

	public void setDate_ri_1(String date_ri_1) {
		this.date_ri_1 = date_ri_1;
	}

	public void setDate_ri_2(String date_ri_2) {
		this.date_ri_2 = date_ri_2;
	}

	public void setAmount_ri(Integer amount_ri) {
		this.amount_ri = amount_ri;
	}

	public void setJenisclaim(Integer jenisclaim) {
		this.jenisclaim = jenisclaim;
	}

	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}

	public void setGroupclaimjenis(String groupclaimjenis) {
		this.groupclaimjenis = groupclaimjenis;
	}

	public void setAccountno(String accountno) {
		this.accountno = accountno;
	}

	public void setLsbp_id(Integer lsbp_id) {
		this.lsbp_id = lsbp_id;
	}

	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}

	public void setAtas_nama(String atas_nama) {
		this.atas_nama = atas_nama;
	}

	public void setDouble_cover_claim(Boolean double_cover_claim) {
		this.double_cover_claim = double_cover_claim;
	}

	public void setJenis_tertanggung(String jenis_tertanggung) {
		this.jenis_tertanggung = jenis_tertanggung;
	}

	public void setLanguage_id(Integer language_id) {
		this.language_id = language_id;
	}
}