package com.app.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.mapper.VegaMapper;
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
import com.app.model.DropdownPolicyAlteration;
import com.app.model.Endorse;
import com.app.model.Fund;
import com.app.model.Inbox;
import com.app.model.KlaimKesehatan;
import com.app.model.LstHistActivityWS;
import com.app.model.LstUserSimultaneous;
import com.app.model.MpolisConfiguration;
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
import com.app.model.Sales;
import com.app.model.SmsServerOut;
import com.app.model.StableLink;
import com.app.model.StableSave;
import com.app.model.SwitchingRedirection;
import com.app.model.Tertanggung;
import com.app.model.TertanggungTambahan;
import com.app.model.Topup;
import com.app.model.TrackingPolis;
import com.app.model.UnitLink;
import com.app.model.User;
import com.app.model.UserCorporate;
import com.app.model.VersionCode;
import com.app.model.ViewClaim;
import com.app.model.Withdraw;
import com.app.model.request.RequestTrackingPolis;

@Component
public class VegaServices {
	@Autowired
	private SqlSession sqlSession;
	//VegaMapper
	
	// Stored Procedure
	public void storedProcedureGetBiaya(String reg_spaj, String kode_regis, Integer kode_trans, BigDecimal amount,
			Integer proses, Integer flag_insert) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
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
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("kode_trans", kode_trans);
		hashMap.put("hasil", null);
		dao.storedProcedureSubmitFinancialTransaction(hashMap);
	}

	// Insert
	public void insertLstHistActvWs(LstHistActivityWS lstHistActivityWS) throws Exception {
		try {
			VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
			dao.insertLstHistActvWs(lstHistActivityWS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertSmsServerOut(SmsServerOut smsOut) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertSmsServerOut(smsOut);
	}

	public void insertNewuser(LstUserSimultaneous lstUserSimultaneous) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertNewuser(lstUserSimultaneous);
	}

	public void insertMstMpolTrans(Topup topup) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertMstMpolTrans(topup);
	}

	public void insertMstMpolTransDet(Topup topup) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertMstMpolTransDet(topup);
	}

	public void insertSwitching(String mpt_id_switching, String date_created_java1, String reg_spaj, Integer lt_id,
			String lku_id, BigDecimal mpt_jumlah, BigDecimal mpt_unit, String date_created_java2, String payor_name,
			String working_days) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
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
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("msen_endors_no", msen_endors_no);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("msen_alasan", msen_alasan);
		hashMap.put("lspd_id", lspd_id);
		dao.insertEndorse(hashMap);
	}

	public void insertRedirection(String mpt_id_redirection, String date_created_java1, String reg_spaj, Integer lt_id,
			String lku_id, String date_created_java2, String payor_name, String mpt_id_switching) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
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
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
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
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
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
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
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
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
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
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
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
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
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
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
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
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
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
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> params = new HashMap<>();
		List<MpolisConfiguration> list = dao.selectMpolisConfiguration();
		for (MpolisConfiguration c : list) {
			params.put(c.getConf_name(), c.getConf_value());
		}
		return params;
	}

	public String selectEncrypt(String value) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectEncrypt(value);
	}

	public LstUserSimultaneous selectLoginAuthenticate(LstUserSimultaneous lstUserSimultaneous) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectLoginAuthenticate(lstUserSimultaneous);
	}

	public Pemegang selectNomorPolis(Pemegang pemegang) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectNomorPolis(pemegang);
	}

	public Pemegang selectNomorPolisNotRegister(Pemegang pemegang) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectNomorPolisNotRegister(pemegang);
	}

	public Pemegang selectKtp(String mspe_no_identity) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectKtp(mspe_no_identity);
	}

	public User decryptPassword(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.decryptPassword(user);
	}

	public Pemegang selectPemegang(Pemegang pemegang) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPemegang(pemegang);
	}
	
	public ArrayList<PenerimaManfaat> selectPenerimaManfaat(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPenerimaManfaat(no_polis);
	}

	public DataUsulan selectDataUsulan(DataUsulan dataUsulan) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDataUsulan(dataUsulan);
	}
	
	public Sales selectSales(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectSales(no_polis);
	}

	public ArrayList<TertanggungTambahan> selectTertanggungTambahan(String spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectTertanggungTambahan(spaj);
	}

	public ArrayList<ProductRider> selectProductRider(String spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectProductRider(spaj);
	}

	public Tertanggung selectTertanggung(Tertanggung tertanggung) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectTertanggung(tertanggung);
	}

	public ArrayList<User> selectDetailedPolis(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDetailedPolis(username);
	}

	public ArrayList<UnitLink> selectUnitLink(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectUnitLink(reg_spaj);
	}

	public ArrayList<UnitLink> selectDetailUnitLink(String spaj, String lji_id, Integer pageNumber, Integer pageSize,
			String startDate, String endDate) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
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
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListNav();
	}

	public ArrayList<Nav> selectDetailNav(Integer lji_id, Integer nilai) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lji_id", lji_id);
		hashMap.put("nilai", nilai);
		return dao.selectDetailNav(hashMap);
	}

	public User selectForgotUsernameIndividual(String ktp_or_nopolis, String dob) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("ktp_or_nopolis", ktp_or_nopolis);
		hashMap.put("dob", dob);
		return dao.selectForgotUsernameIndividual(hashMap);
	}

	public User selectForgotPassword(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectForgotPassword(user);
	}

	public Integer selectCountPhoneNumber(String phone_no) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountPhoneNumber(phone_no);
	}

	public ArrayList<KlaimKesehatan> selectKlaimkesehatan(String spaj, Integer pageNumber, Integer pageSize,
			String startDate, String endDate) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
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
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		Integer data = dao.selectCountIdSimultanByIdSimultan(id_simultan);
		if (data.equals(1)) {
			flag = true;
		}
		return flag;
	}

	public ArrayList<Provider> selectProvider() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectProvider();
	}

	public Pemegang selectGetSPAJ(Pemegang pemegang) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetSPAJ(pemegang);
	}

	public String getKodeCabang(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.getKodeCabang(no_polis);
	}

	public ArrayList<Billing> selectBilling(String reg_spaj, Integer pageNumber, Integer pageSize, String startDate,
			String endDate) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		hashMap.put("startDate", startDate);
		hashMap.put("endDate", endDate);
		return dao.selectBilling(hashMap);
	}

	public Integer selectCheckUsername(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckUsername(username);
	}

	public ArrayList<Provinsi> selectListProvinsi() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListProvinsi();
	}

	public ArrayList<Provinsi> selectListKabupaten(Integer lspr_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		return dao.selectListKabupaten(hashMap);
	}

	public ArrayList<Provinsi> selectListKecamatan(Integer lspr_id, Integer lska_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		hashMap.put("lska_id", lska_id);
		return dao.selectListKecamatan(hashMap);
	}

	public ArrayList<Provinsi> selectListKelurahan(Integer lspr_id, Integer lska_id, Integer lskc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		hashMap.put("lska_id", lska_id);
		hashMap.put("lskc_id", lskc_id);
		return dao.selectListKelurahan(hashMap);
	}

	public ArrayList<Provinsi> selectListKodePos(Integer lspr_id, Integer lska_id, Integer lskc_id, Integer lskl_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		hashMap.put("lska_id", lska_id);
		hashMap.put("lskc_id", lskc_id);
		hashMap.put("lskl_id", lskl_id);
		return dao.selectListKodePos(hashMap);
	}

	public ArrayList<Topup> selectListTopup(String reg_spaj, Integer pageNumber, Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListTopup(hashMap);
	}

	public Topup selectListTopupUsingID(String mpt_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListTopupUsingID(mpt_id);
	}

	public Topup selectViewTopupPaper(String mpt_id, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectViewTopupPaper(hashMap);
	}

	public ArrayList<Topup> selectListInvestasiTopup(String mpt_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListInvestasiTopup(mpt_id);
	}
	
	public ArrayList<Topup> selectListInvestasi(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListInvestasi(no_polis);
	}

	public Integer selectCountMessageInboxUnread(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountMessageInboxUnread(username);
	}

	public Integer selectCheckDeathClaim(String id_simultan) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckDeathClaim(id_simultan);
	}

	public Topup selectProductforTopup(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectProductforTopup(no_polis);
	}

	public ArrayList<Fund> selectFundOfProduct(Topup topup) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectFundOfProduct(topup);
	}

	public ArrayList<Fund> selectFundOfProductSwitching(String lsbs_id, String lku_id, String reg_spaj,
			ArrayList<String> lji_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lsbs_id", lsbs_id);
		hashMap.put("lku_id", lku_id);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("lji_id", lji_id);
		return dao.selectFundOfProductSwitching(hashMap);
	}

	public ArrayList<PowerSave> selectPowerSave(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPowerSave(user);
	}

	public ArrayList<StableSave> selectStableSave(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectStableSave(user);
	}

	public ArrayList<StableLink> selectStableLink(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectStableLink(user);
	}

	public Rekening selectRekeningForTopup(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectRekeningForTopup(reg_spaj);
	}

	public ArrayList<StableLink> selectDetailStableLink(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDetailStableLink(user);
	}

	public BigInteger selectGetMptId() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetMptId();
	}
	
	public Integer selectGetLspdId(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetLspdId(reg_spaj);
	}
	
	public String selectGetNoEndors() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetNoEndors();
	}

	public BigInteger selectGetMpcId() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetMpcId();
	}

	public User selectUserIndividual(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectUserIndividual(username);
	}

	public Provinsi selectProvinsiResult(Provinsi provinsi) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectProvinsiResult(provinsi);
	}

	public BigDecimal selectSumJumlahTopup(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectSumJumlahTopup(reg_spaj);
	}

	public Topup selectStartEndActivePolicy(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectStartEndActivePolicy(reg_spaj);
	}

	public Integer selectCountDeathClaim(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountDeathClaim(username);
	}

	public ArrayList<TrackingPolis> selectTrackingPolis(RequestTrackingPolis requestTrackingPolis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectTrackingPolis(requestTrackingPolis);
	}

	public ArrayList<TrackingPolis> selectCountDataTrackingPolis(RequestTrackingPolis requestTrackingPolis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountDataTrackingPolis(requestTrackingPolis);
	}

	public Integer selectPersentaseBiayaTopup(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPersentaseBiayaTopup(reg_spaj);
	}

	public User selectDataForEmailCS(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDataForEmailCS(username);
	}

	public Integer selectCountLstUlangan(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountLstUlangan(username);
	}

	public ArrayList<Fund> selectFundDefaultUser(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectFundDefaultUser(reg_spaj);
	}

	public Integer selectCountIdSimultanByUsername(String mcl_first, String mspe_date_birth) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashmap = new HashMap<>();
		hashmap.put("mcl_first", mcl_first);
		hashmap.put("mspe_date_birth", mspe_date_birth);
		return dao.selectCountIdSimultanByUsername(hashmap);
	}

	public User selectDataUser(String username, String reg_spaj, String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("username", username);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("no_polis", no_polis);
		return dao.selectDataUser(hashMap);
	}

	public ArrayList<VersionCode> selectVersionCode(Integer flag_app, Integer flag_platform) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashmap = new HashMap<>();
		hashmap.put("flag_app", flag_app);
		hashmap.put("flag_platform", flag_platform);
		return dao.selectVersionCode(hashmap);
	}

	public Provinsi selectProvinsi2(Integer lspr_id, Integer lska_id, Integer lskc_id, Integer lskl_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		hashMap.put("lska_id", lska_id);
		hashMap.put("lskc_id", lskc_id);
		hashMap.put("lskl_id", lskl_id);
		return dao.selectProvinsi2(hashMap);
	}

	public User selectBasicInformationForFinancialTransaction(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectBasicInformationForFinancialTransaction(no_polis);
	}

	public ClaimSubmission selectBasicInformationForClaimSubmission(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectBasicInformationForClaimSubmission(no_polis);
	}

	public ArrayList<SwitchingRedirection> selectListSwitchingAndRedirection(String reg_spaj, Integer pageNumber,
			Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListSwitchingAndRedirection(hashMap);
	}
	
	public ArrayList<SwitchingRedirection> selectListSwitching(String reg_spaj, Integer pageNumber,
			Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListSwitching(hashMap);
	}
	
	public ArrayList<SwitchingRedirection> selectListRedirection(String reg_spaj, Integer pageNumber,
			Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListRedirection(hashMap);
	}
	
	public String selectFlagTrans(String mpt_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectFlagTrans(mpt_id);
	}

	public SwitchingRedirection selectViewSwitchingPaper(String mpt_id, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("arrayData", mpt_id);
		return dao.selectViewSwitchingPaper(hashMap);
	}
	
	public SwitchingRedirection selectViewSwitchingPaper_1(ArrayList<String> arrayData, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("arrayData", arrayData);
		return dao.selectViewSwitchingPaper_1(hashMap);
	}

	public ArrayList<SwitchingRedirection> selectViewDetailSwitchingPaper(String mpt_id, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mpt_id", mpt_id);
		return dao.selectViewDetailSwitchingPaper(hashMap);
	}

	public SwitchingRedirection selectViewSwitchingRedirection1(String mpt_id, String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection1(hashMap);
	}
	
	public SwitchingRedirection selectViewSwitchingRedirection1_1(ArrayList<String> arrayData, String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("arrayData", arrayData);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection1_1(hashMap);
	}

	public ArrayList<SwitchingRedirection> selectViewSwitchingRedirection2_2(ArrayList<String> arrayData,
			String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("arrayData", arrayData);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection2_2(hashMap);
	}

	public ArrayList<SwitchingRedirection> selectViewSwitchingRedirection2(String mpt_id,
			String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection2(hashMap);
	}

	public ArrayList<SwitchingRedirection> selectViewSwitchingRedirection3(String mpt_id, String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection3(hashMap);
	}

	public Integer selectCheckStatusTransaction(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectCheckStatusTransaction(hashMap);
	}

	public ArrayList<CostFinancialTransaction> selectBiayaForFinancialTransaction(String reg_spaj, String mpt_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mpt_id", mpt_id);
		return dao.selectBiayaForFinancialTransaction(hashMap);
	}

	public ArrayList<Withdraw> selectListWithdraw(String reg_spaj, Integer pageNumber, Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListWithdraw(hashMap);
	}

	public Withdraw selectViewWithdraw(String mpt_id, String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		hashMap.put("mpt_id", mpt_id);
		return dao.selectViewWithdraw(hashMap);
	}

	public ArrayList<DetailWithdraw> selectViewDetailWithdrawMPolis(String mpt_id, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectViewDetailWithdrawMPolis(hashMap);
	}

	public ArrayList<Withdraw> selectSaldoWithdraw(String reg_spaj) throws InterruptedException {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectSaldoWithdraw(reg_spaj);
	}

	public Withdraw selectDataFormWithdraw(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDataFormWithdraw(reg_spaj);
	}

	public String selectCheckOTP(String no_hp, Integer menu_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_hp", no_hp);
		hashMap.put("menu_id", menu_id);
		return dao.selectCheckOTP(hashMap);
	}

	public Withdraw selectViewWithdrawPaper(String mpt_id, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectViewWithdrawPaper(hashMap);
	}

	public ArrayList<DetailWithdraw> selectViewDetailWithdrawPaper(String mpt_id, String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewDetailWithdrawPaper(hashMap);
	}

	public ArrayList<Topup> selectInvestasiTopupPaper(String mpt_id, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectInvestasiTopupPaper(hashMap);
	}

	public Integer selectCountTransId(String mpt_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountTransId(mpt_id);
	}

	public ArrayList<ClaimSubmission> selectListClaimSubmission(String reg_spaj, Integer pageNumber, Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListClaimSubmission(hashMap);
	}
	
	public ArrayList<ClaimLimit> selectClaimLimit(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		return dao.selectClaimLimit(hashMap);
	}

	public ClaimSubmission selectViewClaimsubmission(String reg_spaj, BigInteger mpc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mpc_id", mpc_id);
		return dao.selectViewClaimsubmission(hashMap);
	}

	public ArrayList<ClaimSubmission> selectAllTertanggungAndProductClaimSubmission(String reg_spaj, Integer type,
			String nama) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("type", type);
		hashMap.put("nama", nama);
		return dao.selectAllTertanggungAndProductClaimSubmission(hashMap);
	}

	public ArrayList<ClaimSubmission> selectChooseJenisClaim(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectChooseJenisClaim(reg_spaj);
	}

	public ClaimSubmission selectGeneratePdfClaim(Integer type, String reg_spaj, String nama) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("type", type);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("nama", nama);
		return dao.selectGeneratePdfClaim(hashMap);
	}

	public Integer selectCountTransIdClaim(String mpc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountTransIdClaim(mpc_id);
	}

	public Integer selectCheckDate(String dateVal) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckDate(dateVal);
	}

	public Integer selectCheckBiayaTransaksi(String mpt_id, String reg_spaj, Integer lt_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("lt_id", lt_id);
		return dao.selectCheckBiayaTransaksi(hashMap);
	}

	public String selectCutoffTransactionLink() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCutoffTransactionLink();
	};

	public String selectCutoffTransactionMagnaAndPrimeLink() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCutoffTransactionMagnaAndPrimeLink();
	}

	public ProductUtama selectProductCode(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectProductCode(reg_spaj);
	}

	public Rekening selectCheckRekeningNasabahIndividu(String mspo_policy_no) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckRekeningNasabahIndividu(mspo_policy_no);
	}

	// Corporate
	public UserCorporate selectCheckUserCorporateRegister(String no_polis, String kode, String dob) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		hashMap.put("kode", kode);
		hashMap.put("dob", dob);
		return dao.selectCheckUserCorporateRegister(hashMap);
	}

	public Integer selectCheckUserCorporateExistsMpolis(String mcl_id_employee) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mcl_id_employee", mcl_id_employee);
		return dao.selectCheckUserCorporateExistsMpolis(hashMap);
	}

	public Integer selectCheckUserCorporatePhoneNumberExistsMpolis(String no_hp) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckUserCorporatePhoneNumberExistsMpolis(no_hp);
	}

	public ArrayList<UserCorporate> selectListPolisCorporate(String mcl_id_employee) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mcl_id_employee", mcl_id_employee);
		return dao.selectListPolisCorporate(hashMap);
	}

	public LstUserSimultaneous selectDataLstUserSimultaneous(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDataLstUserSimultaneous(username);
	}

	public UserCorporate selectUserCorporate(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectUserCorporate(username);
	}

	public ArrayList<ClaimCorporate> selectListClaimCorporate(String reg_spaj, String mste_insured, Integer pageNumber,
			Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListClaimCorporate(hashMap);
	}

	public ArrayList<DetailClaimCorporate> selectDetailClaimCorporate(String no_claim) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDetailClaimCorporate(no_claim);
	}

	public ArrayList<BenefitCorporate> selectListBenefitCorporate(String reg_spaj, String mste_insured) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		return dao.selectListBenefitCorporate(hashMap);
	}

	public ArrayList<Article> selectListArticle(Integer pageNumber, Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListArticle(hashMap);
	}

	public UserCorporate selectBegDateEndDateCorporate(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectBegDateEndDateCorporate(no_polis);
	}

	public UserCorporate selectForgotUsernameCorporate(String no_polis, String kode, String dob) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		hashMap.put("kode", kode);
		hashMap.put("dob", dob);
		return dao.selectForgotUsernameCorporate(hashMap);
	}

	public ClaimSubmissionCorporate selectClaimSubmissionCorporate(String reg_spaj, String mste_insured) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		return dao.selectClaimSubmissionCorporate(hashMap);
	}

	public ArrayList<ClaimSubmissionCorporate> selectJenisClaimSubmissionCorporate(String reg_spaj,
			String mste_insured) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		return dao.selectJenisClaimSubmissionCorporate(hashMap);
	}

	public ArrayList<ClaimSubmissionCorporate> selectDocumentJenisClaimCorporate(String value, Integer language_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("value", value);
		hashMap.put("language_id", language_id);
		return dao.selectDocumentJenisClaimCorporate(hashMap);
	}

	public ArrayList<ClaimSubmissionCorporate> selectListClaimSubmissionCorporate(String reg_spaj, String mste_insured,
			Integer pageNumber, Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListClaimSubmissionCorporate(hashMap);
	}

	public ClaimSubmissionCorporate selectViewClaimSubmissionCorporate(String reg_spaj, String mste_insured,
			String mpcc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		hashMap.put("mpcc_id", mpcc_id);
		return dao.selectViewClaimSubmissionCorporate(hashMap);
	}

	public String selectGetMpccId() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetMpccId();
	}

	public Integer selectCheckMpccId(String mpcc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckMpccId(mpcc_id);
	}

	public Rekening selectCheckRekeningNasabahCorporate(String msbc_spaj, String mcl_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("msbc_spaj", msbc_spaj);
		hashMap.put("mcl_id", mcl_id);
		return dao.selectCheckRekeningNasabahCorporate(hashMap);
	}
	
	public User selectCheckPhoneNumberIndividu(String mspo_policy_no) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckPhoneNumberIndividu(mspo_policy_no);
	}

	public UserCorporate selectCheckPhoneNumberCorporate(String reg_spaj, String mste_insured) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		return dao.selectCheckPhoneNumberCorporate(hashMap);
	}

	// Update
	public void updateUserKeyName(LstUserSimultaneous user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateUserKeyName(user);
	}
	
	public void updateLspdId(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateLspdId(reg_spaj);
	}

	public void updatePassword(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updatePassword(user);
	}

	public void updateDataMstAddressNew(Pemegang pemegang) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateDataMstAddressNew(pemegang);
	}

	public void updateDataMstAddressBilling(Pemegang pemegang) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateDataMstAddressBilling(pemegang);
	}

	public void updateKeyLoginClear(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateKeyLoginClear(user);
	}

	public void deleteAllInbox(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.deleteAllInbox(user);
	}

	public void updateInboxStatus(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateInboxStatus(user);
	}

	public void updateActivityStatus(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateActivityStatus(user);
	}

	public void updateLinkAccount(String reg_spaj, String id_simultan, String mcl_id_employee, String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		LstUserSimultaneous lstUserSimultaneous = new LstUserSimultaneous();
		lstUserSimultaneous.setREG_SPAJ(reg_spaj);
		lstUserSimultaneous.setID_SIMULTAN(id_simultan);
		lstUserSimultaneous.setMCL_ID_EMPLOYEE(mcl_id_employee);
		lstUserSimultaneous.setUSERNAME(username);
		dao.updateLinkAccount(lstUserSimultaneous);
	}

	public void updateUnlinkAccountCorporate(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		UserCorporate userCorporate = new UserCorporate();
		userCorporate.setUsername(username);
		dao.updateUnlinkAccountCorporate(userCorporate);
	}
	
	public void updateFurtherClaimIndividu(String mpc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateFurtherClaimIndividu(mpc_id);
	}
	
	public void updateFurtherClaimCorporate(String mpcc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateFurtherClaimCorporate(mpcc_id);
	}
	
	public String selectCheckOTP(String no_hp, Integer jenis_id, Integer menu_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_hp", no_hp);
		hashMap.put("jenis_id", jenis_id);
		hashMap.put("menu_id", menu_id);
		return dao.selectCheckOTP(hashMap);
	}
	
	public Endorse selectGetPremiumHolidayDate(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetPremiumHolidayDate(reg_spaj);
	}

	public Endorse selectListPremiumHoliday(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListPremiumHoliday(reg_spaj);
	}

	public ArrayList<Beneficiary> selectListBeneficiary(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListBeneficiary(no_polis);
	}

	public PolicyAlteration selectPayor(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPayor(no_polis);
	}

	public PolicyAlteration selectInsured(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectInsured(no_polis);
	}

	public PolicyAlteration selectPolicyHolder(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPolicyHolder(no_polis);
	}

	public PolicyAlteration selectKorespondensi(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectKorespondensi(no_polis);
	}

	public ArrayList<Endorse> selectListPolicyAlteration(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListPolicyAlteration(reg_spaj);
	}

	public ArrayList<DropdownPolicyAlteration> selectListPernikahan() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListPernikahan();
	}
	
	public ArrayList<DropdownPolicyAlteration> selectListAgama() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListAgama();
	}
	
	public ArrayList<DropdownPolicyAlteration> selectListNegara() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListNegara();
	}
	
	public ArrayList<DropdownPolicyAlteration> selectListJenisUsaha() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListJenisUsaha();
	}
	
	public ArrayList<DropdownPolicyAlteration> selectListPekerjaan() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListPekerjaan();
	}
	
	public ArrayList<DropdownPolicyAlteration> selectListBank() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListBank();
	}
	
	public ArrayList<DropdownPolicyAlteration> selectListRelation() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListRelation();
	}

	public Endorse selectListJenisEndors(Integer lsje_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListJenisEndors(lsje_id);
	}

	public void updateAgama(PolicyAlteration policyAlteration) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateAgama(policyAlteration);
	}
	
	public void insertLstUlangan(String reg_spaj, String msen_alasan) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("msen_alasan", msen_alasan);
		dao.insertLstUlangan(hashMap);
	}

	public String selectMclId_PP(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectMclId_PP(reg_spaj);
	}

	public void updateAlamatKantor(PolicyAlteration policyAlteration) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateAlamatKantor(policyAlteration);
	}

	public void updateKewarganegaraan(PolicyAlteration policyAlteration) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateKewarganegaraan(policyAlteration);
	}

	public void updateStatus(PolicyAlteration policyAlteration) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateStatus(policyAlteration);
	}

	public void updateJenisPekerjaan(PolicyAlteration policyAlteration) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateJenisPekerjaan(policyAlteration);
	}

	public PembayarPremi selectPembayarPremi(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPembayarPremi(no_polis);
	}

	public ArrayList<Inbox> selectInbox(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectInbox(username);
	}

	public NotifToken selectNotifToken(String userid) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectNotifToken(userid);
	}

	public void updateNotifToken(NotifToken notifToken) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateNotifToken(notifToken);
	}

	public void insertNotifToken(NotifToken notifToken_new) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertNotifToken(notifToken_new);
	}

	public void insertNotification(PushNotif pushNotif) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertNotification(pushNotif);
	}
}
