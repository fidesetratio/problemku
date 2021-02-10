package com.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class DataUsulan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 713869903523320781L;

	private Date mste_tgl_recur;
	private String lstp_produk;
	private String no_polis;
	private String reg_spaj;
	private Integer mste_insured_no;
	private Integer mspr_unit;
	private Integer mspr_class;
	private Double mspr_tsi;
	private Double mspr_premium;
	private Integer mspr_ins_period;
	private Date mspr_end_pay;
	private Integer mspr_tt;
	private Integer lsbs_id;
	private String lsdbs_name;
	private String newname;
	private Integer mspo_pay_period;
	private Integer mste_medical;
	private String medis;
	private Date mste_end_date;
	private Date mste_beg_date;
	private Integer mste_flag_cc;
	private Integer lscb_id;
	private String lscb_pay_mode;
	private String lku_id;
	private String kurs_p;
	private String lku_symbol;
	private String plan;
	private String kurs_premi;
	private Integer lsdbs_number;
	private Integer jmlrider;
	private Integer jumlah_seluruh_rider;
	private Integer jmlrider_include;
	private Integer li_umur_ttg;
	private Integer li_umur_pp;
	private Integer kode_flag;
	private Integer flag_as;
	private Integer flag_bao;
	private Double rate_plan;
	private Integer mn;
	private Double total_premi_rider;
	private Double total_premi_sementara;
	private Integer flag_account;
	private ArrayList daftaRider;
	private ArrayList daftabenef;
	private Integer jml_benef;
	private Integer isProductBancass;
	private Integer isInvestasi;
	private Integer isBungaSimponi;
	private Integer isBonusTahapan;
	private Integer flag_rider;
	public Integer flag_jenis_plan;
	public Integer flag_platinumlink;
	public Integer indeks_validasi;
	public Integer tipeproduk;
	private Integer li_trans_tunggal;
	private Integer li_trans_berkala;
	private Double jmlh_top;
	private Integer flag_worksite;
	private String kodeproduk;
	private Integer mspo_installment;
	private Integer flag_endowment;
	private Integer flag_bulanan;
	private Double mspr_discount;
	private String mspo_policy_no_format;
	private String mata_uang;
	private Double mu_jlh_tu;
	private String cara_bayar;
	private String lsbp_nama;
	private Date mar_expired;
	private Date last_premi;
	private Date next_premi;

	public Date getMste_tgl_recur() {
		return mste_tgl_recur;
	}

	public void setMste_tgl_recur(Date mste_tgl_recur) {
		this.mste_tgl_recur = mste_tgl_recur;
	}

	public String getLstp_produk() {
		return lstp_produk;
	}

	public void setLstp_produk(String lstp_produk) {
		this.lstp_produk = lstp_produk;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public Integer getMste_insured_no() {
		return mste_insured_no;
	}

	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}

	public Integer getMspr_unit() {
		return mspr_unit;
	}

	public void setMspr_unit(Integer mspr_unit) {
		this.mspr_unit = mspr_unit;
	}

	public Integer getMspr_class() {
		return mspr_class;
	}

	public void setMspr_class(Integer mspr_class) {
		this.mspr_class = mspr_class;
	}

	public Double getMspr_tsi() {
		return mspr_tsi;
	}

	public void setMspr_tsi(Double mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}

	public Double getMspr_premium() {
		return mspr_premium;
	}

	public void setMspr_premium(Double mspr_premium) {
		this.mspr_premium = mspr_premium;
	}

	public Integer getMspr_ins_period() {
		return mspr_ins_period;
	}

	public void setMspr_ins_period(Integer mspr_ins_period) {
		this.mspr_ins_period = mspr_ins_period;
	}

	public Date getMspr_end_pay() {
		return mspr_end_pay;
	}

	public void setMspr_end_pay(Date mspr_end_pay) {
		this.mspr_end_pay = mspr_end_pay;
	}

	public Integer getMspr_tt() {
		return mspr_tt;
	}

	public void setMspr_tt(Integer mspr_tt) {
		this.mspr_tt = mspr_tt;
	}

	public Integer getLsbs_id() {
		return lsbs_id;
	}

	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}

	public String getLsdbs_name() {
		return lsdbs_name;
	}

	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}

	public String getNewname() {
		return newname;
	}

	public void setNewname(String newname) {
		this.newname = newname;
	}

	public Integer getMspo_pay_period() {
		return mspo_pay_period;
	}

	public void setMspo_pay_period(Integer mspo_pay_period) {
		this.mspo_pay_period = mspo_pay_period;
	}

	public Integer getMste_medical() {
		return mste_medical;
	}

	public void setMste_medical(Integer mste_medical) {
		this.mste_medical = mste_medical;
	}

	public String getMedis() {
		return medis;
	}

	public void setMedis(String medis) {
		this.medis = medis;
	}

	public Date getMste_end_date() {
		return mste_end_date;
	}

	public void setMste_end_date(Date mste_end_date) {
		this.mste_end_date = mste_end_date;
	}

	public Date getMste_beg_date() {
		return mste_beg_date;
	}

	public void setMste_beg_date(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
	}

	public Integer getMste_flag_cc() {
		return mste_flag_cc;
	}

	public void setMste_flag_cc(Integer mste_flag_cc) {
		this.mste_flag_cc = mste_flag_cc;
	}

	public Integer getLscb_id() {
		return lscb_id;
	}

	public void setLscb_id(Integer lscb_id) {
		this.lscb_id = lscb_id;
	}

	public String getLscb_pay_mode() {
		return lscb_pay_mode;
	}

	public void setLscb_pay_mode(String lscb_pay_mode) {
		this.lscb_pay_mode = lscb_pay_mode;
	}

	public String getLku_id() {
		return lku_id;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public String getKurs_p() {
		return kurs_p;
	}

	public void setKurs_p(String kurs_p) {
		this.kurs_p = kurs_p;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getKurs_premi() {
		return kurs_premi;
	}

	public void setKurs_premi(String kurs_premi) {
		this.kurs_premi = kurs_premi;
	}

	public Integer getLsdbs_number() {
		return lsdbs_number;
	}

	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}

	public Integer getJmlrider() {
		return jmlrider;
	}

	public void setJmlrider(Integer jmlrider) {
		this.jmlrider = jmlrider;
	}

	public Integer getJumlah_seluruh_rider() {
		return jumlah_seluruh_rider;
	}

	public void setJumlah_seluruh_rider(Integer jumlah_seluruh_rider) {
		this.jumlah_seluruh_rider = jumlah_seluruh_rider;
	}

	public Integer getJmlrider_include() {
		return jmlrider_include;
	}

	public void setJmlrider_include(Integer jmlrider_include) {
		this.jmlrider_include = jmlrider_include;
	}

	public Integer getLi_umur_ttg() {
		return li_umur_ttg;
	}

	public void setLi_umur_ttg(Integer li_umur_ttg) {
		this.li_umur_ttg = li_umur_ttg;
	}

	public Integer getLi_umur_pp() {
		return li_umur_pp;
	}

	public void setLi_umur_pp(Integer li_umur_pp) {
		this.li_umur_pp = li_umur_pp;
	}

	public Integer getKode_flag() {
		return kode_flag;
	}

	public void setKode_flag(Integer kode_flag) {
		this.kode_flag = kode_flag;
	}

	public Integer getFlag_as() {
		return flag_as;
	}

	public void setFlag_as(Integer flag_as) {
		this.flag_as = flag_as;
	}

	public Integer getFlag_bao() {
		return flag_bao;
	}

	public void setFlag_bao(Integer flag_bao) {
		this.flag_bao = flag_bao;
	}

	public Double getRate_plan() {
		return rate_plan;
	}

	public void setRate_plan(Double rate_plan) {
		this.rate_plan = rate_plan;
	}

	public Integer getMn() {
		return mn;
	}

	public void setMn(Integer mn) {
		this.mn = mn;
	}

	public Double getTotal_premi_rider() {
		return total_premi_rider;
	}

	public void setTotal_premi_rider(Double total_premi_rider) {
		this.total_premi_rider = total_premi_rider;
	}

	public Double getTotal_premi_sementara() {
		return total_premi_sementara;
	}

	public void setTotal_premi_sementara(Double total_premi_sementara) {
		this.total_premi_sementara = total_premi_sementara;
	}

	public Integer getFlag_account() {
		return flag_account;
	}

	public void setFlag_account(Integer flag_account) {
		this.flag_account = flag_account;
	}

	public ArrayList getDaftaRider() {
		return daftaRider;
	}

	public void setDaftaRider(ArrayList daftaRider) {
		this.daftaRider = daftaRider;
	}

	public ArrayList getDaftabenef() {
		return daftabenef;
	}

	public void setDaftabenef(ArrayList daftabenef) {
		this.daftabenef = daftabenef;
	}

	public Integer getJml_benef() {
		return jml_benef;
	}

	public void setJml_benef(Integer jml_benef) {
		this.jml_benef = jml_benef;
	}

	public Integer getIsProductBancass() {
		return isProductBancass;
	}

	public void setIsProductBancass(Integer isProductBancass) {
		this.isProductBancass = isProductBancass;
	}

	public Integer getIsInvestasi() {
		return isInvestasi;
	}

	public void setIsInvestasi(Integer isInvestasi) {
		this.isInvestasi = isInvestasi;
	}

	public Integer getIsBungaSimponi() {
		return isBungaSimponi;
	}

	public void setIsBungaSimponi(Integer isBungaSimponi) {
		this.isBungaSimponi = isBungaSimponi;
	}

	public Integer getIsBonusTahapan() {
		return isBonusTahapan;
	}

	public void setIsBonusTahapan(Integer isBonusTahapan) {
		this.isBonusTahapan = isBonusTahapan;
	}

	public Integer getFlag_rider() {
		return flag_rider;
	}

	public void setFlag_rider(Integer flag_rider) {
		this.flag_rider = flag_rider;
	}

	public Integer getFlag_jenis_plan() {
		return flag_jenis_plan;
	}

	public void setFlag_jenis_plan(Integer flag_jenis_plan) {
		this.flag_jenis_plan = flag_jenis_plan;
	}

	public Integer getFlag_platinumlink() {
		return flag_platinumlink;
	}

	public void setFlag_platinumlink(Integer flag_platinumlink) {
		this.flag_platinumlink = flag_platinumlink;
	}

	public Integer getIndeks_validasi() {
		return indeks_validasi;
	}

	public void setIndeks_validasi(Integer indeks_validasi) {
		this.indeks_validasi = indeks_validasi;
	}

	public Integer getTipeproduk() {
		return tipeproduk;
	}

	public void setTipeproduk(Integer tipeproduk) {
		this.tipeproduk = tipeproduk;
	}

	public Integer getLi_trans_tunggal() {
		return li_trans_tunggal;
	}

	public void setLi_trans_tunggal(Integer li_trans_tunggal) {
		this.li_trans_tunggal = li_trans_tunggal;
	}

	public Integer getLi_trans_berkala() {
		return li_trans_berkala;
	}

	public void setLi_trans_berkala(Integer li_trans_berkala) {
		this.li_trans_berkala = li_trans_berkala;
	}

	public Double getJmlh_top() {
		return jmlh_top;
	}

	public void setJmlh_top(Double jmlh_top) {
		this.jmlh_top = jmlh_top;
	}

	public Integer getFlag_worksite() {
		return flag_worksite;
	}

	public void setFlag_worksite(Integer flag_worksite) {
		this.flag_worksite = flag_worksite;
	}

	public String getKodeproduk() {
		return kodeproduk;
	}

	public void setKodeproduk(String kodeproduk) {
		this.kodeproduk = kodeproduk;
	}

	public Integer getMspo_installment() {
		return mspo_installment;
	}

	public void setMspo_installment(Integer mspo_installment) {
		this.mspo_installment = mspo_installment;
	}

	public Integer getFlag_endowment() {
		return flag_endowment;
	}

	public void setFlag_endowment(Integer flag_endowment) {
		this.flag_endowment = flag_endowment;
	}

	public Integer getFlag_bulanan() {
		return flag_bulanan;
	}

	public void setFlag_bulanan(Integer flag_bulanan) {
		this.flag_bulanan = flag_bulanan;
	}

	public Double getMspr_discount() {
		return mspr_discount;
	}

	public void setMspr_discount(Double mspr_discount) {
		this.mspr_discount = mspr_discount;
	}

	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}

	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}

	public String getMata_uang() {
		return mata_uang;
	}

	public void setMata_uang(String mata_uang) {
		this.mata_uang = mata_uang;
	}

	public Double getMu_jlh_tu() {
		return mu_jlh_tu;
	}

	public void setMu_jlh_tu(Double mu_jlh_tu) {
		this.mu_jlh_tu = mu_jlh_tu;
	}

	public String getCara_bayar() {
		return cara_bayar;
	}

	public void setCara_bayar(String cara_bayar) {
		this.cara_bayar = cara_bayar;
	}

	public String getLsbp_nama() {
		return lsbp_nama;
	}

	public void setLsbp_nama(String lsbp_nama) {
		this.lsbp_nama = lsbp_nama;
	}

	public Date getMar_expired() {
		return mar_expired;
	}

	public void setMar_expired(Date mar_expired) {
		this.mar_expired = mar_expired;
	}

	public Date getLast_premi() {
		return last_premi;
	}

	public void setLast_premi(Date last_premi) {
		this.last_premi = last_premi;
	}

	public Date getNext_premi() {
		return next_premi;
	}

	public void setNext_premi(Date next_premi) {
		this.next_premi = next_premi;
	}
}
