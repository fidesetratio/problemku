package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1600080806560083184L;
	private String ip;
	private int otp_no;
	private String mcl_id;
	private Date mspe_date_birth;
	private String mspo_policy_no;
	private String ktp_or_nopolis;
	private String lji_id;
	private String key_name;
	private String no_polis;
	private String id;
	private String new_username;
	private String username;
	private String key;
	private String password;
	private Date loginTime;
	private String username_or_email;
	private String no_hp;
	private String no_hp2;
	private String phone_no;
	private String new_password;
	private Integer jenis; // 1 = individu, 3 = mri, 4 = dmtm
	private String dob;// tanggal lahir user
	private String email;
	private Integer flag_active;
	private Date date_active;
	private String ip_active;
	private Integer luh_id;
	private String identity;
	private String holder_name;
	private Date change_date;
	private String warning;
	private Integer lue_id;
	private String mspe_email;
	private String new_pass;
	private String conf_pass;
	private String password_decrypt;
	private String id_simultan;
	private Date date_expired;
	private Integer attempt;
	private Integer id_otp;
	private Integer request_code;
	private String file_name;
	private String mspe_no_identity;
	private String alamat_rumah;
	private String last_login_device;
	private Integer pageSize;
	private Integer pageNumber;
	private String startDate;
	private String endDate;
	private Integer inbox_id;
	private String new_status;
	private String kota_rumah;
	private String kd_pos_rumah;
	private Integer lspr_id;
	private Integer lska_id;
	private Integer lskc_id;
	private Integer lskl_id;
	private String kabupaten;
	private Integer status_product;
	private String mpt_id;
	private String token;
	private String userid;
	private String reg_spaj;
	private Integer msl_tu_ke;
	private Date update_date;
	private String date_created_java;
	private Integer typeUpdatePassword;
	private Integer language_id;
	private String mcl_first;
	private String mspe_mother;
	private String lku_id;
	private Integer lsbs_id;
	private String nm_pemegang;
	private String status_polis;
	private String nm_product;
	private String lku_symbol;
	private String rekening;
	private String bank;
	private String enable_cuti_premi;

	private String mspo_policy_no_format;
	private BigDecimal gprod_id;
	private BigDecimal ishealth;
	private String nm_pp;
	private String nm_tt;
	private String status;
	private BigDecimal lms_id;

	public String getIp() {
		return ip;
	}

	public int getOtp_no() {
		return otp_no;
	}

	public String getMcl_id() {
		return mcl_id;
	}

	public Date getMspe_date_birth() {
		return mspe_date_birth;
	}

	public String getMspo_policy_no() {
		return mspo_policy_no;
	}

	public String getKtp_or_nopolis() {
		return ktp_or_nopolis;
	}

	public String getLji_id() {
		return lji_id;
	}

	public String getKey_name() {
		return key_name;
	}

	public String getNo_polis() {
		return no_polis;
	}

	public String getId() {
		return id;
	}

	public String getNew_username() {
		return new_username;
	}

	public String getUsername() {
		return username;
	}

	public String getKey() {
		return key;
	}

	public String getPassword() {
		return password;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public String getUsername_or_email() {
		return username_or_email;
	}

	public String getNo_hp() {
		return no_hp;
	}

	public String getNo_hp2() {
		return no_hp2;
	}

	public String getPhone_no() {
		return phone_no;
	}

	public String getNew_password() {
		return new_password;
	}

	public Integer getJenis() {
		return jenis;
	}

	public String getDob() {
		return dob;
	}

	public String getEmail() {
		return email;
	}

	public Integer getFlag_active() {
		return flag_active;
	}

	public Date getDate_active() {
		return date_active;
	}

	public String getIp_active() {
		return ip_active;
	}

	public Integer getLuh_id() {
		return luh_id;
	}

	public String getIdentity() {
		return identity;
	}

	public String getHolder_name() {
		return holder_name;
	}

	public Date getChange_date() {
		return change_date;
	}

	public String getWarning() {
		return warning;
	}

	public Integer getLue_id() {
		return lue_id;
	}

	public String getMspe_email() {
		return mspe_email;
	}

	public String getNew_pass() {
		return new_pass;
	}

	public String getConf_pass() {
		return conf_pass;
	}

	public String getPassword_decrypt() {
		return password_decrypt;
	}

	public String getId_simultan() {
		return id_simultan;
	}

	public Date getDate_expired() {
		return date_expired;
	}

	public Integer getAttempt() {
		return attempt;
	}

	public Integer getId_otp() {
		return id_otp;
	}

	public Integer getRequest_code() {
		return request_code;
	}

	public String getFile_name() {
		return file_name;
	}

	public String getMspe_no_identity() {
		return mspe_no_identity;
	}

	public String getAlamat_rumah() {
		return alamat_rumah;
	}

	public String getLast_login_device() {
		return last_login_device;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public Integer getInbox_id() {
		return inbox_id;
	}

	public String getNew_status() {
		return new_status;
	}

	public String getKota_rumah() {
		return kota_rumah;
	}

	public String getKd_pos_rumah() {
		return kd_pos_rumah;
	}

	public Integer getLspr_id() {
		return lspr_id;
	}

	public Integer getLska_id() {
		return lska_id;
	}

	public Integer getLskc_id() {
		return lskc_id;
	}

	public Integer getLskl_id() {
		return lskl_id;
	}

	public String getKabupaten() {
		return kabupaten;
	}

	public Integer getStatus_product() {
		return status_product;
	}

	public String getMpt_id() {
		return mpt_id;
	}

	public String getToken() {
		return token;
	}

	public String getUserid() {
		return userid;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public Integer getMsl_tu_ke() {
		return msl_tu_ke;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public String getDate_created_java() {
		return date_created_java;
	}

	public Integer getTypeUpdatePassword() {
		return typeUpdatePassword;
	}

	public Integer getLanguage_id() {
		return language_id;
	}

	public String getMcl_first() {
		return mcl_first;
	}

	public String getMspe_mother() {
		return mspe_mother;
	}

	public String getLku_id() {
		return lku_id;
	}

	public Integer getLsbs_id() {
		return lsbs_id;
	}

	public String getNm_pemegang() {
		return nm_pemegang;
	}

	public String getStatus_polis() {
		return status_polis;
	}

	public String getNm_product() {
		return nm_product;
	}

	public String getLku_symbol() {
		return lku_symbol;
	}

	public String getRekening() {
		return rekening;
	}

	public String getBank() {
		return bank;
	}

	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}

	public BigDecimal getGprod_id() {
		return gprod_id;
	}

	public BigDecimal getIshealth() {
		return ishealth;
	}

	public String getNm_pp() {
		return nm_pp;
	}

	public String getNm_tt() {
		return nm_tt;
	}

	public String getStatus() {
		return status;
	}

	public BigDecimal getLms_id() {
		return lms_id;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setOtp_no(int otp_no) {
		this.otp_no = otp_no;
	}

	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}

	public void setMspe_date_birth(Date mspe_date_birth) {
		this.mspe_date_birth = mspe_date_birth;
	}

	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}

	public void setKtp_or_nopolis(String ktp_or_nopolis) {
		this.ktp_or_nopolis = ktp_or_nopolis;
	}

	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}

	public void setKey_name(String key_name) {
		this.key_name = key_name;
	}

	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNew_username(String new_username) {
		this.new_username = new_username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public void setUsername_or_email(String username_or_email) {
		this.username_or_email = username_or_email;
	}

	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}

	public void setNo_hp2(String no_hp2) {
		this.no_hp2 = no_hp2;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}

	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFlag_active(Integer flag_active) {
		this.flag_active = flag_active;
	}

	public void setDate_active(Date date_active) {
		this.date_active = date_active;
	}

	public void setIp_active(String ip_active) {
		this.ip_active = ip_active;
	}

	public void setLuh_id(Integer luh_id) {
		this.luh_id = luh_id;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public void setHolder_name(String holder_name) {
		this.holder_name = holder_name;
	}

	public void setChange_date(Date change_date) {
		this.change_date = change_date;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public void setLue_id(Integer lue_id) {
		this.lue_id = lue_id;
	}

	public void setMspe_email(String mspe_email) {
		this.mspe_email = mspe_email;
	}

	public void setNew_pass(String new_pass) {
		this.new_pass = new_pass;
	}

	public void setConf_pass(String conf_pass) {
		this.conf_pass = conf_pass;
	}

	public void setPassword_decrypt(String password_decrypt) {
		this.password_decrypt = password_decrypt;
	}

	public void setId_simultan(String id_simultan) {
		this.id_simultan = id_simultan;
	}

	public void setDate_expired(Date date_expired) {
		this.date_expired = date_expired;
	}

	public void setAttempt(Integer attempt) {
		this.attempt = attempt;
	}

	public void setId_otp(Integer id_otp) {
		this.id_otp = id_otp;
	}

	public void setRequest_code(Integer request_code) {
		this.request_code = request_code;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public void setMspe_no_identity(String mspe_no_identity) {
		this.mspe_no_identity = mspe_no_identity;
	}

	public void setAlamat_rumah(String alamat_rumah) {
		this.alamat_rumah = alamat_rumah;
	}

	public void setLast_login_device(String last_login_device) {
		this.last_login_device = last_login_device;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setInbox_id(Integer inbox_id) {
		this.inbox_id = inbox_id;
	}

	public void setNew_status(String new_status) {
		this.new_status = new_status;
	}

	public void setKota_rumah(String kota_rumah) {
		this.kota_rumah = kota_rumah;
	}

	public void setKd_pos_rumah(String kd_pos_rumah) {
		this.kd_pos_rumah = kd_pos_rumah;
	}

	public void setLspr_id(Integer lspr_id) {
		this.lspr_id = lspr_id;
	}

	public void setLska_id(Integer lska_id) {
		this.lska_id = lska_id;
	}

	public void setLskc_id(Integer lskc_id) {
		this.lskc_id = lskc_id;
	}

	public void setLskl_id(Integer lskl_id) {
		this.lskl_id = lskl_id;
	}

	public void setKabupaten(String kabupaten) {
		this.kabupaten = kabupaten;
	}

	public void setStatus_product(Integer status_product) {
		this.status_product = status_product;
	}

	public void setMpt_id(String mpt_id) {
		this.mpt_id = mpt_id;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public void setMsl_tu_ke(Integer msl_tu_ke) {
		this.msl_tu_ke = msl_tu_ke;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public void setDate_created_java(String date_created_java) {
		this.date_created_java = date_created_java;
	}

	public void setTypeUpdatePassword(Integer typeUpdatePassword) {
		this.typeUpdatePassword = typeUpdatePassword;
	}

	public void setLanguage_id(Integer language_id) {
		this.language_id = language_id;
	}

	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}

	public void setMspe_mother(String mspe_mother) {
		this.mspe_mother = mspe_mother;
	}

	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}

	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}

	public void setNm_pemegang(String nm_pemegang) {
		this.nm_pemegang = nm_pemegang;
	}

	public void setStatus_polis(String status_polis) {
		this.status_polis = status_polis;
	}

	public void setNm_product(String nm_product) {
		this.nm_product = nm_product;
	}

	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}

	public void setRekening(String rekening) {
		this.rekening = rekening;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}

	public void setGprod_id(BigDecimal gprod_id) {
		this.gprod_id = gprod_id;
	}

	public void setIshealth(BigDecimal ishealth) {
		this.ishealth = ishealth;
	}

	public void setNm_pp(String nm_pp) {
		this.nm_pp = nm_pp;
	}

	public void setNm_tt(String nm_tt) {
		this.nm_tt = nm_tt;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setLms_id(BigDecimal lms_id) {
		this.lms_id = lms_id;
	}

	public String getEnable_cuti_premi() {
		return enable_cuti_premi;
	}

	public void setEnable_cuti_premi(String enable_cuti_premi) {
		this.enable_cuti_premi = enable_cuti_premi;
	}
}