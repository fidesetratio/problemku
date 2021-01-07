package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ClaimSubmission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5577393152312579813L;
	private BigInteger kode_trans;
	private String no_polis;
	private String reg_spaj;
	private Integer mste_insured_no;
	private String lku_id;
	private BigInteger lsbs_id;
	private BigInteger lsdbs_number;
	private String nm_pemegang;
	private Integer lssp_id;
	private String status_polis;
	private String lku_symbol;
	private String mrc_cabang;
	private String rekening;
	private String bank;
	private String no_hp;
	private String email;
	private String lsbp_id;
	private String nm_product;
	private String nm_product_claim;
	private Date regapldate;
	private String jenisclaim;
	private String status;
	private Date date_update_status;
	private BigDecimal amt_claim;
	private Date date_ri_1;
	private Date date_ri_2;
	private String patienname;
	private String path_claim;
	private String nama_tertanggung;
	private String atas_nama_rekening;
	private String groupclaimjenis;
	private Integer double_cover_claim;
	private String jenis_tertanggung;
	private String reason;
	private String date_status;
	private String reason_further;

	private String nama;
	private BigDecimal umur;
	private String relasi;
	private String alamat_rumah;
	private String kota_rumah;

	private String ljp_id;
	private String lca_id;
	private BigDecimal flag_susulan;
	private BigDecimal id_status;

	public BigInteger getKode_trans() {
		return kode_trans;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public Integer getMste_insured_no() {
		return mste_insured_no;
	}

	public String getLku_id() {
		return lku_id;
	}

	public BigInteger getLsbs_id() {
		return lsbs_id;
	}

	public BigInteger getLsdbs_number() {
		return lsdbs_number;
	}

	public String getNm_pemegang() {
		return nm_pemegang;
	}

	public Integer getLssp_id() {
		return lssp_id;
	}

	public String getStatus_polis() {
		return status_polis;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public String getMrc_cabang() {
		return mrc_cabang;
	}

	public String getRekening() {
		return rekening;
	}

	public String getBank() {
		return bank;
	}

	public String getNo_hp() {
		return no_hp;
	}

	public String getEmail() {
		return email;
	}

	public String getLsbp_id() {
		return lsbp_id;
	}

	public String getNm_product() {
		return nm_product;
	}

	public String getNm_product_claim() {
		return nm_product_claim;
	}

	public Date getRegapldate() {
		return regapldate;
	}

	public String getJenisclaim() {
		return jenisclaim;
	}

	public String getStatus() {
		return status;
	}

	public Date getDate_update_status() {
		return date_update_status;
	}

	public BigDecimal getAmt_claim() {
		return amt_claim;
	}

	public Date getDate_ri_1() {
		return date_ri_1;
	}

	public Date getDate_ri_2() {
		return date_ri_2;
	}

	public String getPatienname() {
		return patienname;
	}

	public String getPath_claim() {
		return path_claim;
	}

	public String getNama_tertanggung() {
		return nama_tertanggung;
	}

	public String getAtas_nama_rekening() {
		return atas_nama_rekening;
	}

	public String getGroupclaimjenis() {
		return groupclaimjenis;
	}

	public Integer getDouble_cover_claim() {
		return double_cover_claim;
	}

	public String getJenis_tertanggung() {
		return jenis_tertanggung;
	}

	public String getReason() {
		return reason;
	}

	public String getDate_status() {
		return date_status;
	}

	public String getReason_further() {
		return reason_further;
	}

	public String getNama() {
		return nama;
	}

	public BigDecimal getUmur() {
		return umur;
	}

	public String getRelasi() {
		return relasi;
	}

	public String getAlamat_rumah() {
		return alamat_rumah;
	}

	public String getKota_rumah() {
		return kota_rumah;
	}

	public String getLjp_id() {
		return ljp_id;
	}

	public String getLca_id() {
		return lca_id;
	}

	public BigDecimal getFlag_susulan() {
		return flag_susulan;
	}

	public BigDecimal getId_status() {
		return id_status;
	}

	public void setKode_trans(BigInteger kode_trans) {
		this.kode_trans = kode_trans;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public void setLsbs_id(BigInteger lsbs_id) {
		this.lsbs_id = lsbs_id;
	}

	public void setLsdbs_number(BigInteger lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}

	public void setNm_pemegang(String nm_pemegang) {
		this.nm_pemegang = nm_pemegang;
	}

	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}

	public void setStatus_polis(String status_polis) {
		this.status_polis = status_polis;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public void setMrc_cabang(String mrc_cabang) {
		this.mrc_cabang = mrc_cabang;
	}

	public void setRekening(String rekening) {
		this.rekening = rekening;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLsbp_id(String lsbp_id) {
		this.lsbp_id = lsbp_id;
	}

	public void setNm_product(String nm_product) {
		this.nm_product = nm_product;
	}

	public void setNm_product_claim(String nm_product_claim) {
		this.nm_product_claim = nm_product_claim;
	}

	public void setRegapldate(Date regapldate) {
		this.regapldate = regapldate;
	}

	public void setJenisclaim(String jenisclaim) {
		this.jenisclaim = jenisclaim;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDate_update_status(Date date_update_status) {
		this.date_update_status = date_update_status;
	}

	public void setAmt_claim(BigDecimal amt_claim) {
		this.amt_claim = amt_claim;
	}

	public void setDate_ri_1(Date date_ri_1) {
		this.date_ri_1 = date_ri_1;
	}

	public void setDate_ri_2(Date date_ri_2) {
		this.date_ri_2 = date_ri_2;
	}

	public void setPatienname(String patienname) {
		this.patienname = patienname;
	}

	public void setPath_claim(String path_claim) {
		this.path_claim = path_claim;
	}

	public void setNama_tertanggung(String nama_tertanggung) {
		this.nama_tertanggung = nama_tertanggung;
	}

	public void setAtas_nama_rekening(String atas_nama_rekening) {
		this.atas_nama_rekening = atas_nama_rekening;
	}

	public void setGroupclaimjenis(String groupclaimjenis) {
		this.groupclaimjenis = groupclaimjenis;
	}

	public void setDouble_cover_claim(Integer double_cover_claim) {
		this.double_cover_claim = double_cover_claim;
	}

	public void setJenis_tertanggung(String jenis_tertanggung) {
		this.jenis_tertanggung = jenis_tertanggung;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setDate_status(String date_status) {
		this.date_status = date_status;
	}

	public void setReason_further(String reason_further) {
		this.reason_further = reason_further;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public void setUmur(BigDecimal umur) {
		this.umur = umur;
	}

	public void setRelasi(String relasi) {
		this.relasi = relasi;
	}

	public void setAlamat_rumah(String alamat_rumah) {
		this.alamat_rumah = alamat_rumah;
	}

	public void setKota_rumah(String kota_rumah) {
		this.kota_rumah = kota_rumah;
	}

	public void setLjp_id(String ljp_id) {
		this.ljp_id = ljp_id;
	}

	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}

	public void setFlag_susulan(BigDecimal flag_susulan) {
		this.flag_susulan = flag_susulan;
	}

	public void setId_status(BigDecimal id_status) {
		this.id_status = id_status;
	}
}