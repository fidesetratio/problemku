package com.app.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.app.mapper.VegaMapperProd;
import com.app.model.Article;
import com.app.model.Beneficiary;
import com.app.model.BenefitCorporate;
import com.app.model.Billing;
import com.app.model.ClaimCorporate;
import com.app.model.ClaimLimit;
import com.app.model.ClaimSubmission;
import com.app.model.ClaimSubmissionCorporate;
import com.app.model.CostFinancialTransaction;
import com.app.model.DataUsulan;
import com.app.model.DetailClaimCorporate;
import com.app.model.DetailWithdraw;
import com.app.model.DownloadReportHr;
import com.app.model.DropdownPolicyAlteration;
import com.app.model.Endorse;
import com.app.model.EndorseHr;
import com.app.model.Fund;
import com.app.model.Inbox;
import com.app.model.KlaimKesehatan;
import com.app.model.LstHistActivityWS;
import com.app.model.LstUserSimultaneous;
import com.app.model.MpolisConfiguration;
import com.app.model.MstOTPSimultaneous;
import com.app.model.MstOTPSimultaneousDet;
import com.app.model.Nav;
import com.app.model.NotifToken;
import com.app.model.PembayarPremi;
import com.app.model.Pemegang;
import com.app.model.PenerimaManfaat;
import com.app.model.PolicyAlteration;
import com.app.model.PowerSave;
import com.app.model.ProductRider;
import com.app.model.ProductUtama;
import com.app.model.Provider;
import com.app.model.Provinsi;
import com.app.model.PushNotif;
import com.app.model.Rekening;
import com.app.model.ReportHr;
import com.app.model.Sales;
import com.app.model.SmsServerOut;
import com.app.model.StableLink;
import com.app.model.StableSave;
import com.app.model.SwitchingRedirection;
import com.app.model.Tertanggung;
import com.app.model.TertanggungTambahan;
import com.app.model.Topup;
import com.app.model.TrackingPolis;
import com.app.model.TransactionHistory;
import com.app.model.UnitLink;
import com.app.model.User;
import com.app.model.UserCorporate;
import com.app.model.UserHR;
import com.app.model.VersionCode;
import com.app.model.ViewClaim;
import com.app.model.ViewMclFirst;
import com.app.model.Withdraw;
import com.app.model.request.RequestTrackingPolis;

@Component
public class VegaServicesProd {
	@Autowired
	@Qualifier("sqlSessionProd")
	private SqlSession sqlSession;
	//VegaMapperProd
	
	// Stored Procedure
	public void storedProcedureGetBiaya(String reg_spaj, String kode_regis, Integer kode_trans, BigDecimal amount,
			Integer proses, Integer flag_insert) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("reg_spaj", reg_spaj);
		params.put("kode_regis", kode_regis);
		params.put("kode_trans", kode_trans);
		params.put("amount", amount);
		params.put("proses", proses);
		params.put("flag_insert", flag_insert);
		params.put("hasil", null);
		dao.storedProcedureGetBiaya(params);
	}

	public void storedProcedureSubmitFinancialTransaction(String reg_spaj, String kode_trans) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("kode_trans", kode_trans);
		hashMap.put("hasil", null);
		dao.storedProcedureSubmitFinancialTransaction(hashMap);
	}

	// Insert
	public void insertLstHistActvWs(LstHistActivityWS lstHistActivityWS) throws Exception {
		try {
			VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
			dao.insertLstHistActvWs(lstHistActivityWS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertSmsServerOut(SmsServerOut smsOut) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.insertSmsServerOut(smsOut);
	}

	public void insertNewuser(LstUserSimultaneous lstUserSimultaneous) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.insertNewuser(lstUserSimultaneous);
	}

	public void insertMstMpolTrans(Topup topup) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.insertMstMpolTrans(topup);
	}

	public void insertMstMpolTransDet(Topup topup) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.insertMstMpolTransDet(topup);
	}

	public void insertSwitching(String mpt_id_switching, String date_created_java1, String reg_spaj, Integer lt_id,
			String lku_id, BigDecimal mpt_jumlah, BigDecimal mpt_unit, String date_created_java2, String payor_name,
			String working_days) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id_switching", mpt_id_switching);
		hashMap.put("date_created_java1", date_created_java1);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("lt_id", lt_id);
		hashMap.put("lku_id", lku_id);
		hashMap.put("mpt_jumlah", mpt_jumlah);
		hashMap.put("mpt_unit", mpt_unit);
		hashMap.put("date_created_java2", date_created_java2);
		hashMap.put("payor_name", payor_name);
		hashMap.put("working_days", working_days);
		dao.insertSwitching(hashMap);
//			return (Integer) hashMap.get("MPT_ID");
	}
	
	public void insertEndorse(String msen_endors_no, String reg_spaj, String msen_alasan, Integer lspd_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("msen_endors_no", msen_endors_no);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("msen_alasan", msen_alasan);
		hashMap.put("lspd_id", lspd_id);
		dao.insertEndorse(hashMap);
	}

	public void insertRedirection(String mpt_id_redirection, String date_created_java1, String reg_spaj, Integer lt_id,
			String lku_id, String date_created_java2, String payor_name, String mpt_id_switching) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id_redirection", mpt_id_redirection);
		hashMap.put("date_created_java1", date_created_java1);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("lt_id", lt_id);
		hashMap.put("lku_id", lku_id);
		hashMap.put("date_created_java2", date_created_java2);
		hashMap.put("payor_name", payor_name);
		hashMap.put("mpt_id_switching", mpt_id_switching);
		dao.insertRedirection(hashMap);
//			return (Integer) hashMap.get("MPT_ID");
	}

	public void insertDetailSwitching(String mpt_id, String lji_id, Integer mpt_persen, BigDecimal mpt_jumlah,
			BigDecimal mpt_unit, String mpt_dk, String lji_id_ke, Integer persen_ke, BigDecimal jumlah_ke, BigDecimal unit_ke) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("lji_id", lji_id);
		hashMap.put("mpt_persen", mpt_persen);
		hashMap.put("mpt_jumlah", mpt_jumlah);
		hashMap.put("mpt_unit", mpt_unit);
		hashMap.put("mpt_dk", mpt_dk);
		hashMap.put("lji_id_ke", lji_id_ke);
		hashMap.put("persen_ke", persen_ke);
		hashMap.put("jumlah", jumlah_ke);
		hashMap.put("unit_ke", unit_ke);
		dao.insertDetailSwitching(hashMap);
	}
	
	public void insertDetailEndorse(String msen_endors_no, Integer lsje_id, String msde_old1, String msde_old2, String msde_old3, String msde_old4, String msde_old5, String msde_old6, String msde_new1, String msde_new2, String msde_new3, String msde_new4, String msde_new5, String msde_new6) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("msen_endors_no", msen_endors_no);
		hashMap.put("lsje_id", lsje_id);
		hashMap.put("msde_old1", msde_old1);
		hashMap.put("msde_old2", msde_old2);
		hashMap.put("msde_old3", msde_old3);
		hashMap.put("msde_old4", msde_old4);
		hashMap.put("msde_old5", msde_old5);
		hashMap.put("msde_old6", msde_old6);
		hashMap.put("msde_new1", msde_new1);
		hashMap.put("msde_new2", msde_new2);
		hashMap.put("msde_new3", msde_new3);
		hashMap.put("msde_new4", msde_new4);
		hashMap.put("msde_new5", msde_new5);
		hashMap.put("msde_new6", msde_new6);
		dao.insertDetailEndorse(hashMap);
	}

	public void insertDetailRedirection(String mpt_id, String lji_id, Float mpt_persen, String mpt_dk) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("lji_id", lji_id);
		hashMap.put("mpt_persen", mpt_persen);
		hashMap.put("mpt_dk", mpt_dk);
		dao.insertDetailRedirection(hashMap);
	}

	public void insertWithdraw(String mpt_id, String req_date_java, String reg_spaj, Integer lt_id, String lku_id,
			BigDecimal mpt_jumlah, BigDecimal mpt_unit, String created_date_java, String payor_name, String rekening,
			String bank_name, String path_bsb, String working_days) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("req_date", req_date_java);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("lt_id", lt_id);
		hashMap.put("lku_id", lku_id);
		hashMap.put("mpt_jumlah", mpt_jumlah);
		hashMap.put("mpt_unit", mpt_unit);
		hashMap.put("created_date", created_date_java);
		hashMap.put("payor_name", payor_name);
		hashMap.put("path_bsb", path_bsb);
		hashMap.put("rekening", rekening);
		hashMap.put("bank_name", bank_name);
		hashMap.put("working_days", working_days);
		dao.insertWithdraw(hashMap);
	}

	public void insertDetailWithdraw(String mpt_id, String lji_id, BigDecimal mpt_jumlah, BigDecimal mpt_unit) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("lji_id", lji_id);
		hashMap.put("mpt_jumlah", mpt_jumlah);
		hashMap.put("mpt_unit", mpt_unit);
		dao.insertDetailWithdraw(hashMap);
	}

	public void insertClaimSubmissionTrans(BigInteger mpc_id, String reg_spaj, Integer mste_insured_no,
			String patienname, String lku_id, Integer lsbs_id, Integer lsdbs_number, String date_ri_1, String date_ri_2,
			Integer amount_ri, Integer jenisclaim, String no_hp, String email, String path_claim, Integer lssp_id,
			String groupclaimjenis, Integer double_cover_claim) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpc_id", mpc_id);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured_no", mste_insured_no);
		hashMap.put("patienname", patienname);
		hashMap.put("lku_id", lku_id);
		hashMap.put("lsbs_id", lsbs_id);
		hashMap.put("lsdbs_number", lsdbs_number);
		hashMap.put("date_ri_1", date_ri_1);
		hashMap.put("date_ri_2", date_ri_2);
		hashMap.put("amount_ri", amount_ri);
		hashMap.put("jenisclaim", jenisclaim);
		hashMap.put("no_hp", no_hp);
		hashMap.put("email", email);
		hashMap.put("path_claim", path_claim);
		hashMap.put("lssp_id", lssp_id);
		hashMap.put("groupclaimjenis", groupclaimjenis);
		hashMap.put("double_cover_claim", double_cover_claim);
		dao.insertClaimSubmissionTrans(hashMap);
	}

	public void insertDetailClaimSubmissionTrans(BigInteger mpc_id, String reg_spaj, String accountno, Integer lsbp_id,
			String nama_cabang, String atasnama) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpc_id", mpc_id);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("accountno", accountno);
		hashMap.put("lsbp_id", lsbp_id);
		hashMap.put("nama_cabang", nama_cabang);
		hashMap.put("atasnama", atasnama);
		dao.insertDetailClaimSubmissionTrans(hashMap);
	}

	public void insertClaimSubmissionCorporate(String mpcc_id, String reg_spaj, String mcl_id, String no_reg, Integer lssp_id,
			String jenis_claim, String start_date, String end_date, Integer double_cover_claim, String path_storage,
			String bank, String no_rekening) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpcc_id", mpcc_id);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mcl_id", mcl_id);
		hashMap.put("no_reg", no_reg);
		hashMap.put("lssp_id", lssp_id);
		hashMap.put("jenis_claim", jenis_claim);
		hashMap.put("start_date", start_date);
		hashMap.put("end_date", end_date);
		hashMap.put("double_cover_claim", double_cover_claim);
		hashMap.put("path_storage", path_storage);
		hashMap.put("bank", bank);
		hashMap.put("no_rekening", no_rekening);
		dao.insertClaimSubmissionCorporate(hashMap);
	}

	// Select
	public static ArrayList<Object> serializableList(List<Object> dataList) {
		if (dataList != null) {
			return new ArrayList<>(dataList);
		} else {
			return null;
		}
	}

	public HashMap<String, Object> configuration() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> params = new HashMap<>();
		List<MpolisConfiguration> list = dao.selectMpolisConfiguration();
		for (MpolisConfiguration c : list) {
			params.put(c.getConf_name(), c.getConf_value());
		}
		return params;
	}

	public String selectEncrypt(String value) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectEncrypt(value);
	}

	public LstUserSimultaneous selectLoginAuthenticate(LstUserSimultaneous lstUserSimultaneous) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectLoginAuthenticate(lstUserSimultaneous);
	}

	public Pemegang selectNomorPolis(Pemegang pemegang) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectNomorPolis(pemegang);
	}

	public Pemegang selectNomorPolisNotRegister(Pemegang pemegang) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectNomorPolisNotRegister(pemegang);
	}

	public Pemegang selectKtp(String mspe_no_identity) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectKtp(mspe_no_identity);
	}

	public User decryptPassword(User user) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.decryptPassword(user);
	}

	public Pemegang selectPemegang(Pemegang pemegang) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectPemegang(pemegang);
	}
	
	public ArrayList<PenerimaManfaat> selectPenerimaManfaat(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectPenerimaManfaat(no_polis);
	}

	public DataUsulan selectDataUsulan(DataUsulan dataUsulan) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectDataUsulan(dataUsulan);
	}
	
	public Sales selectSales(String msag_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectSales(msag_id);
	}

	public ArrayList<TertanggungTambahan> selectTertanggungTambahan(String spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectTertanggungTambahan(spaj);
	}

	public ArrayList<ProductRider> selectProductRider(String spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectProductRider(spaj);
	}

	public Tertanggung selectTertanggung(Tertanggung tertanggung) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectTertanggung(tertanggung);
	}

	public ArrayList<User> selectDetailedPolis(String username) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectDetailedPolis(username);
	}

	public ArrayList<UnitLink> selectUnitLink(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectUnitLink(reg_spaj);
	}

	
	public Double selectTotalTunggakanUnitLink(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectTotalTunggakanUnitLink(reg_spaj);
	}
	public ArrayList<UnitLink> selectDetailUnitLink(String spaj, String lji_id, Integer pageNumber, Integer pageSize,
			String startDate, String endDate) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lji_id", lji_id);
		hashMap.put("spaj", spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		hashMap.put("startDate", startDate);
		hashMap.put("endDate", endDate);
		return dao.selectDetailUnitLink(hashMap);
	}

	public ArrayList<Nav> selectListNav() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListNav();
	}

	public ArrayList<Nav> selectDetailNav(Integer lji_id, Integer nilai) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lji_id", lji_id);
		hashMap.put("nilai", nilai);
		return dao.selectDetailNav(hashMap);
	}

	public User selectForgotUsernameIndividual(String ktp_or_nopolis, String dob) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("ktp_or_nopolis", ktp_or_nopolis);
		hashMap.put("dob", dob);
		return dao.selectForgotUsernameIndividual(hashMap);
	}

	public User selectForgotPassword(User user) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectForgotPassword(user);
	}

	public Integer selectCountPhoneNumber(String phone_no) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCountPhoneNumber(phone_no);
	}

	public ArrayList<KlaimKesehatan> selectKlaimkesehatan(String spaj, Integer pageNumber, Integer pageSize,
			String startDate, String endDate) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		ViewClaim viewClaim = new ViewClaim();
		viewClaim.setSpaj(spaj);
		viewClaim.setPageNumber(pageNumber);
		viewClaim.setPageSize(pageSize);
		viewClaim.setStartDate(startDate);
		viewClaim.setEndDate(endDate);
		return dao.selectKlaimkesehatan(viewClaim);
	}

	public boolean selectCountIdSimultanByIdSimultan(String id_simultan) {
		boolean flag = false;
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		Integer data = dao.selectCountIdSimultanByIdSimultan(id_simultan);
		if (data.equals(1)) {
			flag = true;
		}
		return flag;
	}

	public ArrayList<Provider> selectProvider() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectProvider();
	}

	public Pemegang selectGetSPAJ(Pemegang pemegang) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectGetSPAJ(pemegang);
	}

	public String getKodeCabang(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.getKodeCabang(no_polis);
	}

	public ArrayList<Billing> selectBilling(String reg_spaj, Integer pageNumber, Integer pageSize, String startDate,
			String endDate) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		hashMap.put("startDate", startDate);
		hashMap.put("endDate", endDate);
		return dao.selectBilling(hashMap);
	}

	public Integer selectCheckUsername(String username) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCheckUsername(username);
	}

	public ArrayList<Provinsi> selectListProvinsi() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListProvinsi();
	}

	public ArrayList<Provinsi> selectListKabupaten(Integer lspr_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		return dao.selectListKabupaten(hashMap);
	}

	public ArrayList<Provinsi> selectListKecamatan(Integer lspr_id, Integer lska_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		hashMap.put("lska_id", lska_id);
		return dao.selectListKecamatan(hashMap);
	}

	public ArrayList<Provinsi> selectListKelurahan(Integer lspr_id, Integer lska_id, Integer lskc_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		hashMap.put("lska_id", lska_id);
		hashMap.put("lskc_id", lskc_id);
		return dao.selectListKelurahan(hashMap);
	}

	public ArrayList<Provinsi> selectListKodePos(Integer lspr_id, Integer lska_id, Integer lskc_id, Integer lskl_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		hashMap.put("lska_id", lska_id);
		hashMap.put("lskc_id", lskc_id);
		hashMap.put("lskl_id", lskl_id);
		return dao.selectListKodePos(hashMap);
	}

	public ArrayList<Topup> selectListTopup(String reg_spaj, Integer pageNumber, Integer pageSize) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListTopup(hashMap);
	}

	public Topup selectListTopupUsingID(String mpt_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListTopupUsingID(mpt_id);
	}

	public Topup selectViewTopupPaper(String mpt_id, String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectViewTopupPaper(hashMap);
	}

	public ArrayList<Topup> selectListInvestasiTopup(String mpt_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListInvestasiTopup(mpt_id);
	}
	
	public ArrayList<Topup> selectListInvestasi(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListInvestasi(no_polis);
	}

	public Integer selectCountMessageInboxUnread(String username) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCountMessageInboxUnread(username);
	}

	public Integer selectCheckDeathClaim(String id_simultan) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCheckDeathClaim(id_simultan);
	}

	public Topup selectProductforTopup(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectProductforTopup(no_polis);
	}

	public ArrayList<Fund> selectFundOfProduct(Topup topup) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectFundOfProduct(topup);
	}

	public ArrayList<Fund> selectFundOfProductSwitching(String lsbs_id, String lku_id, String reg_spaj,
			ArrayList<String> lji_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lsbs_id", lsbs_id);
		hashMap.put("lku_id", lku_id);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("lji_id", lji_id);
		return dao.selectFundOfProductSwitching(hashMap);
	}

	public ArrayList<PowerSave> selectPowerSave(User user) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectPowerSave(user);
	}

	public ArrayList<StableSave> selectStableSave(User user) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectStableSave(user);
	}

	public ArrayList<StableLink> selectStableLink(User user) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectStableLink(user);
	}

	public Rekening selectRekeningForTopup(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectRekeningForTopup(reg_spaj);
	}

	public ArrayList<StableLink> selectDetailStableLink(User user) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectDetailStableLink(user);
	}

	public BigInteger selectGetMptId() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectGetMptId();
	}
	
	public Integer selectGetLspdId(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectGetLspdId(reg_spaj);
	}
	
	public String selectGetNoEndors() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectGetNoEndors();
	}

	public BigInteger selectGetMpcId() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectGetMpcId();
	}

	public User selectUserIndividual(String username) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectUserIndividual(username);
	}

	public Provinsi selectProvinsiResult(Provinsi provinsi) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectProvinsiResult(provinsi);
	}

	public BigDecimal selectSumJumlahTopup(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectSumJumlahTopup(reg_spaj);
	}

	public Topup selectStartEndActivePolicy(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectStartEndActivePolicy(reg_spaj);
	}

	public Integer selectCountDeathClaim(String username) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCountDeathClaim(username);
	}

	public ArrayList<TrackingPolis> selectTrackingPolis(RequestTrackingPolis requestTrackingPolis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectTrackingPolis(requestTrackingPolis);
	}

	public ArrayList<TrackingPolis> selectCountDataTrackingPolis(RequestTrackingPolis requestTrackingPolis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCountDataTrackingPolis(requestTrackingPolis);
	}

	public Integer selectPersentaseBiayaTopup(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectPersentaseBiayaTopup(reg_spaj);
	}

	public User selectDataForEmailCS(String username) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectDataForEmailCS(username);
	}

	public Integer selectCountLstUlangan(String username) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCountLstUlangan(username);
	}

	public ArrayList<Fund> selectFundDefaultUser(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectFundDefaultUser(reg_spaj);
	}

	public Integer selectCountIdSimultanByUsername(String mcl_first, String mspe_date_birth) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashmap = new HashMap<>();
		hashmap.put("mcl_first", mcl_first);
		hashmap.put("mspe_date_birth", mspe_date_birth);
		return dao.selectCountIdSimultanByUsername(hashmap);
	}

	public User selectDataUser(String username, String reg_spaj, String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("username", username);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("no_polis", no_polis);
		return dao.selectDataUser(hashMap);
	}

	public ArrayList<VersionCode> selectVersionCode(Integer flag_app, Integer flag_platform) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashmap = new HashMap<>();
		hashmap.put("flag_app", flag_app);
		hashmap.put("flag_platform", flag_platform);
		return dao.selectVersionCode(hashmap);
	}

	public Provinsi selectProvinsi2(Integer lspr_id, Integer lska_id, Integer lskc_id, Integer lskl_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		hashMap.put("lska_id", lska_id);
		hashMap.put("lskc_id", lskc_id);
		hashMap.put("lskl_id", lskl_id);
		return dao.selectProvinsi2(hashMap);
	}

	public User selectBasicInformationForFinancialTransaction(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectBasicInformationForFinancialTransaction(no_polis);
	}

	public ClaimSubmission selectBasicInformationForClaimSubmission(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectBasicInformationForClaimSubmission(no_polis);
	}

	public ArrayList<SwitchingRedirection> selectListSwitchingAndRedirection(String reg_spaj, Integer pageNumber,
			Integer pageSize) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListSwitchingAndRedirection(hashMap);
	}
	
	public ArrayList<SwitchingRedirection> selectListSwitching(String reg_spaj, Integer pageNumber,
			Integer pageSize) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListSwitching(hashMap);
	}
	
	public ArrayList<SwitchingRedirection> selectListRedirection(String reg_spaj, Integer pageNumber,
			Integer pageSize) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListRedirection(hashMap);
	}
	
	public String selectFlagTrans(String mpt_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectFlagTrans(mpt_id);
	}

	public SwitchingRedirection selectViewSwitchingPaper(String mpt_id, String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("arrayData", mpt_id);
		return dao.selectViewSwitchingPaper(hashMap);
	}
	
	public SwitchingRedirection selectViewSwitchingPaper_1(ArrayList<String> arrayData, String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("arrayData", arrayData);
		return dao.selectViewSwitchingPaper_1(hashMap);
	}

	public ArrayList<SwitchingRedirection> selectViewDetailSwitchingPaper(String mpt_id, String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mpt_id", mpt_id);
		return dao.selectViewDetailSwitchingPaper(hashMap);
	}

	public SwitchingRedirection selectViewSwitchingRedirection1(String mpt_id, String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection1(hashMap);
	}
	
	public SwitchingRedirection selectViewSwitchingRedirection1_1(ArrayList<String> arrayData, String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("arrayData", arrayData);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection1_1(hashMap);
	}

	public ArrayList<SwitchingRedirection> selectViewSwitchingRedirection2_2(ArrayList<String> arrayData,
			String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("arrayData", arrayData);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection2_2(hashMap);
	}

	public ArrayList<SwitchingRedirection> selectViewSwitchingRedirection2(String mpt_id,
			String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection2(hashMap);
	}

	public ArrayList<SwitchingRedirection> selectViewSwitchingRedirection3(String mpt_id, String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection3(hashMap);
	}

	public Integer selectCheckStatusTransaction(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectCheckStatusTransaction(hashMap);
	}

	public ArrayList<CostFinancialTransaction> selectBiayaForFinancialTransaction(String reg_spaj, String mpt_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mpt_id", mpt_id);
		return dao.selectBiayaForFinancialTransaction(hashMap);
	}

	public ArrayList<Withdraw> selectListWithdraw(String reg_spaj, Integer pageNumber, Integer pageSize) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListWithdraw(hashMap);
	}

	public Withdraw selectViewWithdraw(String mpt_id, String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		hashMap.put("mpt_id", mpt_id);
		return dao.selectViewWithdraw(hashMap);
	}

	public ArrayList<DetailWithdraw> selectViewDetailWithdrawMPolis(String mpt_id, String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectViewDetailWithdrawMPolis(hashMap);
	}

	public ArrayList<Withdraw> selectSaldoWithdraw(String reg_spaj) throws InterruptedException {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectSaldoWithdraw(reg_spaj);
	}

	public Withdraw selectDataFormWithdraw(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectDataFormWithdraw(reg_spaj);
	}

	public String selectCheckOTP(String no_hp, Integer menu_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_hp", no_hp);
		hashMap.put("menu_id", menu_id);
		return dao.selectCheckOTP(hashMap);
	}

	public Withdraw selectViewWithdrawPaper(String mpt_id, String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectViewWithdrawPaper(hashMap);
	}

	public ArrayList<DetailWithdraw> selectViewDetailWithdrawPaper(String mpt_id, String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewDetailWithdrawPaper(hashMap);
	}

	public ArrayList<Topup> selectInvestasiTopupPaper(String mpt_id, String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectInvestasiTopupPaper(hashMap);
	}

	public Integer selectCountTransId(String mpt_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCountTransId(mpt_id);
	}

	public ArrayList<ClaimSubmission> selectListClaimSubmission(String reg_spaj, Integer pageNumber, Integer pageSize) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListClaimSubmission(hashMap);
	}
	
	public ArrayList<ClaimLimit> selectClaimLimit(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		return dao.selectClaimLimit(hashMap);
	}

	public ClaimSubmission selectViewClaimsubmission(String reg_spaj, BigInteger mpc_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mpc_id", mpc_id);
		return dao.selectViewClaimsubmission(hashMap);
	}

	public ArrayList<ClaimSubmission> selectAllTertanggungAndProductClaimSubmission(String reg_spaj, Integer type,
			String nama) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("type", type);
		hashMap.put("nama", nama);
		return dao.selectAllTertanggungAndProductClaimSubmission(hashMap);
	}

	public ArrayList<ClaimSubmission> selectChooseJenisClaim(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectChooseJenisClaim(reg_spaj);
	}

	public ClaimSubmission selectGeneratePdfClaim(Integer type, String reg_spaj, String nama) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("type", type);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("nama", nama);
		return dao.selectGeneratePdfClaim(hashMap);
	}

	public Integer selectCountTransIdClaim(String mpc_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCountTransIdClaim(mpc_id);
	}

	public Integer selectCheckDate(String dateVal) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCheckDate(dateVal);
	}

	public Integer selectCheckBiayaTransaksi(String mpt_id, String reg_spaj, Integer lt_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("lt_id", lt_id);
		return dao.selectCheckBiayaTransaksi(hashMap);
	}

	public String selectCutoffTransactionLink() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCutoffTransactionLink();
	};

	public String selectCutoffTransactionMagnaAndPrimeLink() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCutoffTransactionMagnaAndPrimeLink();
	}

	public ProductUtama selectProductCode(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectProductCode(reg_spaj);
	}

	public Rekening selectCheckRekeningNasabahIndividu(String mspo_policy_no) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCheckRekeningNasabahIndividu(mspo_policy_no);
	}

	// Corporate
	public UserCorporate selectCheckUserCorporateRegister(String no_polis, String kode, String dob) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		hashMap.put("kode", kode);
		hashMap.put("dob", dob);
		return dao.selectCheckUserCorporateRegister(hashMap);
	}

	public Integer selectCheckUserCorporateExistsMpolis(String mcl_id_employee) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mcl_id_employee", mcl_id_employee);
		return dao.selectCheckUserCorporateExistsMpolis(hashMap);
	}

	public Integer selectCheckUserCorporatePhoneNumberExistsMpolis(String no_hp) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCheckUserCorporatePhoneNumberExistsMpolis(no_hp);
	}

	public ArrayList<UserCorporate> selectListPolisCorporate(String mcl_id_employee) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mcl_id_employee", mcl_id_employee);
		return dao.selectListPolisCorporate(hashMap);
	}

	public LstUserSimultaneous selectDataLstUserSimultaneous(String username) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectDataLstUserSimultaneous(username);
	}

	public UserCorporate selectUserCorporate(String username) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectUserCorporate(username);
	}

	public ArrayList<ClaimCorporate> selectListClaimCorporate(String reg_spaj, String mste_insured, Integer pageNumber,
			Integer pageSize) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListClaimCorporate(hashMap);
	}

	public ArrayList<DetailClaimCorporate> selectDetailClaimCorporate(String no_claim) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectDetailClaimCorporate(no_claim);
	}

	public ArrayList<BenefitCorporate> selectListBenefitCorporate(String reg_spaj, String mste_insured) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		return dao.selectListBenefitCorporate(hashMap);
	}

	public ArrayList<Article> selectListArticle(Integer pageNumber, Integer pageSize) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListArticle(hashMap);
	}

	public UserCorporate selectBegDateEndDateCorporate(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectBegDateEndDateCorporate(no_polis);
	}

	public UserCorporate selectForgotUsernameCorporate(String no_polis, String kode, String dob) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		hashMap.put("kode", kode);
		hashMap.put("dob", dob);
		return dao.selectForgotUsernameCorporate(hashMap);
	}

	public ClaimSubmissionCorporate selectClaimSubmissionCorporate(String reg_spaj, String mste_insured) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		return dao.selectClaimSubmissionCorporate(hashMap);
	}

	public ArrayList<ClaimSubmissionCorporate> selectJenisClaimSubmissionCorporate(String reg_spaj,
			String mste_insured) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		return dao.selectJenisClaimSubmissionCorporate(hashMap);
	}

	public ArrayList<ClaimSubmissionCorporate> selectDocumentJenisClaimCorporate(String value, Integer language_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("value", value);
		hashMap.put("language_id", language_id);
		return dao.selectDocumentJenisClaimCorporate(hashMap);
	}

	public ArrayList<ClaimSubmissionCorporate> selectListClaimSubmissionCorporate(String reg_spaj, String mste_insured,
			Integer pageNumber, Integer pageSize) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListClaimSubmissionCorporate(hashMap);
	}

	public ClaimSubmissionCorporate selectViewClaimSubmissionCorporate(String reg_spaj, String mste_insured,
			String mpcc_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		hashMap.put("mpcc_id", mpcc_id);
		return dao.selectViewClaimSubmissionCorporate(hashMap);
	}

	public String selectGetMpccId() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectGetMpccId();
	}

	public Integer selectCheckMpccId(String mpcc_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCheckMpccId(mpcc_id);
	}

	public Rekening selectCheckRekeningNasabahCorporate(String msbc_spaj, String mcl_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("msbc_spaj", msbc_spaj);
		hashMap.put("mcl_id", mcl_id);
		return dao.selectCheckRekeningNasabahCorporate(hashMap);
	}
	
	public User selectCheckPhoneNumberIndividu(String mspo_policy_no) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCheckPhoneNumberIndividu(mspo_policy_no);
	}

	public UserCorporate selectCheckPhoneNumberCorporate(String reg_spaj, String mste_insured) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		return dao.selectCheckPhoneNumberCorporate(hashMap);
	}

	// Update
	public void updateUserKeyName(LstUserSimultaneous user) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateUserKeyName(user);
	}
	
	public void updateLspdId(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateLspdId(reg_spaj);
	}

	public void updatePassword(User user) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updatePassword(user);
	}

	public void updateDataMstAddressNew(Pemegang pemegang) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateDataMstAddressNew(pemegang);
	}

	public void updateDataMstAddressBilling(Pemegang pemegang) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateDataMstAddressBilling(pemegang);
	}

	public void updateKeyLoginClear(User user) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateKeyLoginClear(user);
	}

	public void deleteAllInbox(User user) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.deleteAllInbox(user);
	}

	public void updateInboxStatus(User user) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateInboxStatus(user);
	}

	public void updateActivityStatus(User user) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateActivityStatus(user);
	}

	public void updateLinkAccount(String reg_spaj, String id_simultan, String mcl_id_employee, String username) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		LstUserSimultaneous lstUserSimultaneous = new LstUserSimultaneous();
		lstUserSimultaneous.setREG_SPAJ(reg_spaj);
		lstUserSimultaneous.setID_SIMULTAN(id_simultan);
		lstUserSimultaneous.setMCL_ID_EMPLOYEE(mcl_id_employee);
		lstUserSimultaneous.setUSERNAME(username);
		dao.updateLinkAccount(lstUserSimultaneous);
	}

	public void updateUnlinkAccountCorporate(String username) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		UserCorporate userCorporate = new UserCorporate();
		userCorporate.setUsername(username);
		dao.updateUnlinkAccountCorporate(userCorporate);
	}
	
	public void updateFurtherClaimIndividu(String mpc_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateFurtherClaimIndividu(mpc_id);
	}
	
	public void updateFurtherClaimCorporate(String mpcc_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateFurtherClaimCorporate(mpcc_id);
	}
	
	public String selectCheckOTP(String no_hp, Integer jenis_id, Integer menu_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_hp", no_hp);
		hashMap.put("jenis_id", jenis_id);
		hashMap.put("menu_id", menu_id);
		return dao.selectCheckOTP(hashMap);
	}
	
	public Endorse selectGetPremiumHolidayDate(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectGetPremiumHolidayDate(reg_spaj);
	}

	public Endorse selectListPremiumHoliday(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListPremiumHoliday(reg_spaj);
	}

	public ArrayList<Beneficiary> selectListBeneficiary(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListBeneficiary(no_polis);
	}

	public PolicyAlteration selectPayor(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectPayor(no_polis);
	}

	public PolicyAlteration selectInsured(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectInsured(no_polis);
	}

	public PolicyAlteration selectPolicyHolder(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectPolicyHolder(no_polis);
	}

	public PolicyAlteration selectKorespondensi(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectKorespondensi(no_polis);
	}

	public ArrayList<Endorse> selectListPolicyAlteration(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListPolicyAlteration(reg_spaj);
	}

	public ArrayList<DropdownPolicyAlteration> selectListPernikahan() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListPernikahan();
	}
	
	public ArrayList<DropdownPolicyAlteration> selectListAgama() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListAgama();
	}
	
	public ArrayList<DropdownPolicyAlteration> selectListNegara() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListNegara();
	}
	
	public ArrayList<DropdownPolicyAlteration> selectListJenisUsaha() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListJenisUsaha();
	}
	
	public ArrayList<DropdownPolicyAlteration> selectListPekerjaan() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListPekerjaan();
	}
	
	public ArrayList<DropdownPolicyAlteration> selectListBank() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListBank();
	}
	
	public ArrayList<DropdownPolicyAlteration> selectListRelation() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListRelation();
	}

	public Endorse selectListJenisEndors(Integer lsje_id) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListJenisEndors(lsje_id);
	}

	public void updateAgama(PolicyAlteration policyAlteration) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateAgama(policyAlteration);
	}
	
	public void insertLstUlangan(String reg_spaj, String msen_alasan) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("msen_alasan", msen_alasan);
		dao.insertLstUlangan(hashMap);
	}

	public String selectMclId_PP(String reg_spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectMclId_PP(reg_spaj);
	}

	public void updateAlamatKantor(PolicyAlteration policyAlteration) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateAlamatKantor(policyAlteration);
	}

	public void updateKewarganegaraan(PolicyAlteration policyAlteration) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateKewarganegaraan(policyAlteration);
	}

	public void updateStatus(PolicyAlteration policyAlteration) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateStatus(policyAlteration);
	}

	public void updateJenisPekerjaan(PolicyAlteration policyAlteration) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateJenisPekerjaan(policyAlteration);
	}

	public PembayarPremi selectPembayarPremi(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectPembayarPremi(no_polis);
	}

	public ArrayList<Inbox> selectInbox(String username) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectInbox(username);
	}

	public NotifToken selectNotifToken(String userid) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectNotifToken(userid);
	}

	public void updateNotifToken(NotifToken notifToken) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateNotifToken(notifToken);
	}

	public void insertNotifToken(NotifToken notifToken_new) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.insertNotifToken(notifToken_new);
	}

	public void insertNotification(PushNotif pushNotif) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.insertNotification(pushNotif);
	}

	public ArrayList<DropdownPolicyAlteration> selectCabangBank(Integer lsbp_id_req) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCabangBank(lsbp_id_req);
	}

	public Article selectCountListArticle() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCountListArticle();
	}
	
	public KlaimKesehatan selectCountKlaimkesehatan(String spaj) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		ViewClaim viewClaim = new ViewClaim();
		viewClaim.setSpaj(spaj);
		return dao.selectCountKlaimkesehatan(viewClaim);
	}

	public ArrayList<UserHR> selectListPolisHRUser(String eb_hr_username) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListPolisHRUser(eb_hr_username);
	}

	public ArrayList<UserHR> selectListPesertaHR(String eb_hr_username, String search_policy, Integer search_type, String policy_no) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("eb_hr_username", eb_hr_username);
		hashMap.put("search_policy", search_policy);
		hashMap.put("search_type", search_type);
		hashMap.put("policy_no", policy_no);
		return dao.selectListPesertaHR(hashMap);
	}

	public ArrayList<ReportHr> selectListReportHr(String no_polis, String no_batch, String beg_date, String end_date, Integer pageNumber, Integer pageSize) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		hashMap.put("no_batch", no_batch);
		hashMap.put("beg_date", beg_date);
		hashMap.put("end_date", end_date);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListReportHr(hashMap);
	}

	public ArrayList<DownloadReportHr> selectPathReportHr(String no_batch) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectPathReportHr(no_batch);
	}

	public Integer selectCountReportHr(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectCountReportHr(no_polis);
	}

	public ArrayList<ViewMclFirst> selectListPolisOrion(String msag_id, String mspo_policy_no, String mcl_first) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("msag_id", msag_id);
		hashMap.put("mspo_policy_no", mspo_policy_no);
		hashMap.put("mcl_first", mcl_first);
		return dao.selectListPolisOrion(hashMap);
	}

	public String selectGetIdTicket() {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectGetIdTicket();
	}

	public void insertSubmitEndorseHr(String id_ticket, Integer id_group, String nik_req, String subject, String description) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("id_ticket", id_ticket);
		hashMap.put("id_group", id_group);
		hashMap.put("nik_req", nik_req);
		hashMap.put("subject", subject);
		hashMap.put("description", description);
		dao.insertSubmitEndorseHr(hashMap);
	}

	public void insertSubmitHistoryEndorse(String id_ticket, String history, String update_by) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("id_ticket", id_ticket);
		hashMap.put("history", history);
		hashMap.put("update_by", update_by);
		dao.insertSubmitHistoryEndorse(hashMap);
	}

	public ArrayList<EndorseHr> selectListEndorseHr(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectListEndorseHr(no_polis);
	}

	public EndorseHr selectViewEndorseHr(String id_ticket) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectViewEndorseHr(id_ticket);
	}

	public EndorseHr selectPrepareEndorseHr(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectPrepareEndorseHr(no_polis);
	}

	public ArrayList<TransactionHistory> selectTransactionHistory(String reg_spaj, String startDate, String endDate, String jenis_transaksi) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("startDate", startDate);
		hashMap.put("endDate", endDate);
		hashMap.put("jenis_transaksi", jenis_transaksi);
		return dao.selectTransactionHistory(hashMap);
	}

	public String selectGetOnlyRegSpaj(String no_polis) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectGetOnlyRegSpaj(no_polis);
	}

	public Integer selectCountListBilling(String reg_spaj, String startDate, String endDate) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("startDate", startDate);
		hashMap.put("endDate", endDate);
		return dao.selectCountListBilling(hashMap);
	}
	
	public MstOTPSimultaneousDet selectDetailOTP(MstOTPSimultaneousDet mstOTPSimultaneousDet) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectDetailOTP(mstOTPSimultaneousDet);
	}

	public MstOTPSimultaneous selectDataOTP(MstOTPSimultaneous paramSelectData) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		return dao.selectDataOTP(paramSelectData);
	}

	public void updateOtpUsed(MstOTPSimultaneous paramUpdateOtpUsed) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateOtpUsed(paramUpdateOtpUsed);
	}

	public void updateAttemptOtp(MstOTPSimultaneous paramUpdate) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateAttemptOtp(paramUpdate);
	}

	public void updateStatusAttemptOtp(MstOTPSimultaneous paramUpdate) {
		VegaMapperProd dao = sqlSession.getMapper(VegaMapperProd.class);
		dao.updateStatusAttemptOtp(paramUpdate);
	}
}
