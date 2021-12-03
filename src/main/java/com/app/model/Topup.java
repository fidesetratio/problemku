package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Topup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5985351114713488434L;
	private String mpt_id;
	private Date req_date;
	private String reg_spaj;
	private Integer lt_id;
	private String lku_id;
	private BigDecimal mpt_jumlah;
	private Integer mpt_unit;
	private Integer mpt_status;
	private Date created_date;
	private Date modified_date;
	private Integer lus_id;
	private String payor_name;
	private String payor_occupation;
	private String payor_income;
	private String payor_source_income;
	private String lji_id;
	private Float mpt_persen;
	private String path_bsb;
	private String reason_fu;
	private BigDecimal mpt_jumlah_process;
	private Integer unique_code;
	private String path_sum_payment;

	private Date date_status;
	private String lku_symbol;
	private String description;
	private String mspo_policy_no;
	private String mcl_first;
	private String lji_invest;
	private String lsdbs_name;
	private String mata_uang;
	private Integer lsbs_id;
	private Integer fund_id;
	private String fund_invest;
	private BigDecimal mpt_jumlah_det;
	private String date_created_java1;
	private String date_created_java2;
	private Date start_active;
	private Date one_year;
	private Date end_active;
	private String newname;
	private BigDecimal mtu_jumlah;
	private Float mdu_persen;
	private String topup_payor;
	private Integer lsjb_id;
	private Integer flag_source;

	public String getTopup_payor() {
		return topup_payor;
	}

	public void setTopup_payor(String topup_payor) {
		this.topup_payor = topup_payor;
	}

	public String getMpt_id() {
		return mpt_id;
	}

	public Date getReq_date() {
		return req_date;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public Integer getLt_id() {
		return lt_id;
	}

	public String getLku_id() {
		return lku_id;
	}

	public BigDecimal getMpt_jumlah() {
		return mpt_jumlah;
	}

	public Integer getMpt_unit() {
		return mpt_unit;
	}

	public Integer getMpt_status() {
		return mpt_status;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public Date getModified_date() {
		return modified_date;
	}

	public Integer getLus_id() {
		return lus_id;
	}

	public String getPayor_name() {
		return payor_name;
	}

	public String getPayor_occupation() {
		return payor_occupation;
	}

	public String getPayor_income() {
		return payor_income;
	}

	public String getPayor_source_income() {
		return payor_source_income;
	}

	public String getLji_id() {
		return lji_id;
	}

	public Float getMpt_persen() {
		return mpt_persen;
	}

	public String getPath_bsb() {
		return path_bsb;
	}

	public String getReason_fu() {
		return reason_fu;
	}

	public BigDecimal getMpt_jumlah_process() {
		return mpt_jumlah_process;
	}

	public Integer getUnique_code() {
		return unique_code;
	}

	public Date getDate_status() {
		return date_status;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public String getDescription() {
		return description;
	}

	public String getMspo_policy_no() {
		return mspo_policy_no;
	}

	public String getMcl_first() {
		return mcl_first;
	}

	public String getLji_invest() {
		return lji_invest;
	}

	public String getLsdbs_name() {
		return lsdbs_name;
	}

	public String getMata_uang() {
		return mata_uang;
	}

	public Integer getLsbs_id() {
		return lsbs_id;
	}

	public Integer getFund_id() {
		return fund_id;
	}

	public String getFund_invest() {
		return fund_invest;
	}

	public BigDecimal getMpt_jumlah_det() {
		return mpt_jumlah_det;
	}

	public String getDate_created_java1() {
		return date_created_java1;
	}

	public String getDate_created_java2() {
		return date_created_java2;
	}

	public Date getStart_active() {
		return start_active;
	}

	public Date getOne_year() {
		return one_year;
	}

	public Date getEnd_active() {
		return end_active;
	}

	public String getNewname() {
		return newname;
	}

	public BigDecimal getMtu_jumlah() {
		return mtu_jumlah;
	}

	public void setMpt_id(String mpt_id) {
		this.mpt_id = mpt_id;
	}

	public void setReq_date(Date req_date) {
		this.req_date = req_date;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public void setLt_id(Integer lt_id) {
		this.lt_id = lt_id;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public void setMpt_jumlah(BigDecimal mpt_jumlah) {
		this.mpt_jumlah = mpt_jumlah;
	}

	public void setMpt_unit(Integer mpt_unit) {
		this.mpt_unit = mpt_unit;
	}

	public void setMpt_status(Integer mpt_status) {
		this.mpt_status = mpt_status;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}

	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}

	public void setPayor_name(String payor_name) {
		this.payor_name = payor_name;
	}

	public void setPayor_occupation(String payor_occupation) {
		this.payor_occupation = payor_occupation;
	}

	public void setPayor_income(String payor_income) {
		this.payor_income = payor_income;
	}

	public void setPayor_source_income(String payor_source_income) {
		this.payor_source_income = payor_source_income;
	}

	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}

	public void setMpt_persen(Float mpt_persen) {
		this.mpt_persen = mpt_persen;
	}

	public void setPath_bsb(String path_bsb) {
		this.path_bsb = path_bsb;
	}

	public void setReason_fu(String reason_fu) {
		this.reason_fu = reason_fu;
	}

	public void setMpt_jumlah_process(BigDecimal mpt_jumlah_process) {
		this.mpt_jumlah_process = mpt_jumlah_process;
	}

	public void setUnique_code(Integer unique_code) {
		this.unique_code = unique_code;
	}

	public void setDate_status(Date date_status) {
		this.date_status = date_status;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}

	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}

	public void setLji_invest(String lji_invest) {
		this.lji_invest = lji_invest;
	}

	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}

	public void setMata_uang(String mata_uang) {
		this.mata_uang = mata_uang;
	}

	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}

	public void setFund_id(Integer fund_id) {
		this.fund_id = fund_id;
	}

	public void setFund_invest(String fund_invest) {
		this.fund_invest = fund_invest;
	}

	public void setMpt_jumlah_det(BigDecimal mpt_jumlah_det) {
		this.mpt_jumlah_det = mpt_jumlah_det;
	}

	public void setDate_created_java1(String date_created_java1) {
		this.date_created_java1 = date_created_java1;
	}

	public void setDate_created_java2(String date_created_java2) {
		this.date_created_java2 = date_created_java2;
	}

	public void setStart_active(Date start_active) {
		this.start_active = start_active;
	}

	public void setOne_year(Date one_year) {
		this.one_year = one_year;
	}

	public void setEnd_active(Date end_active) {
		this.end_active = end_active;
	}

	public void setNewname(String newname) {
		this.newname = newname;
	}

	public void setMtu_jumlah(BigDecimal mtu_jumlah) {
		this.mtu_jumlah = mtu_jumlah;
	}

	public Float getMdu_persen() {
		return mdu_persen;
	}

	public void setMdu_persen(Float mdu_persen) {
		this.mdu_persen = mdu_persen;
	}

	public Integer getLsjb_id() {
		return lsjb_id;
	}

	public void setLsjb_id(Integer lsjb_id) {
		this.lsjb_id = lsjb_id;
	}

	public Integer getFlag_source() {
		return flag_source;
	}

	public void setFlag_source(Integer flag_source) {
		this.flag_source = flag_source;
	}

	public String getPath_sum_payment() {
		return path_sum_payment;
	}

	public void setPath_sum_payment(String path_sum_payment) {
		this.path_sum_payment = path_sum_payment;
	}
}